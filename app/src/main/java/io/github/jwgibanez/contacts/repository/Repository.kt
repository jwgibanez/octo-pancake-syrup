package io.github.jwgibanez.contacts.repository

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.database.AppDatabase
import io.github.jwgibanez.contacts.service.RqresService
import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.service.request.UserRequest
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    private val viewModel: ContactsViewModel,
    private val db: AppDatabase,
    private val service: RqresService
) {

    suspend fun fetchUsers(activity: Activity) {
        withContext(Dispatchers.IO) {
            isNetworkConnected(
                activity,
                {
                    service.getUsers().safeSubscribe(object : Observer<Response<User>> {
                        override fun onSubscribe(d: Disposable) {
                            viewModel.loading.postValue(true)
                        }

                        override fun onNext(value: Response<User>) {
                            if (value.total_pages > 1) {
                                for (i in 2..value.total_pages)
                                    GlobalScope.launch {
                                        fetchUsers(activity, i)
                                    }
                            }

                            AppDatabase.databaseWriteExecutor.execute {
                                db.userDao().insertList(value.data)
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewModel.error.postValue(e.message)
                        }

                        override fun onComplete() {
                            viewModel.loading.postValue(false)
                        }
                    })
                },
                { error -> viewModel.error.postValue(error) }
            )
        }
    }

    private suspend fun fetchUsers(activity: Activity, page: Int) {
        withContext(Dispatchers.IO) {
            isNetworkConnected(
                activity,
                {
                    service.getUsersByPage(page = page).safeSubscribe(object : Observer<Response<User>> {
                        override fun onSubscribe(d: Disposable) {
                            viewModel.loading.postValue(true)
                        }

                        override fun onNext(value: Response<User>) {
                            AppDatabase.databaseWriteExecutor.execute {
                                db.userDao().insertList(value.data)
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewModel.error.postValue(e.message)
                        }

                        override fun onComplete() {
                            viewModel.loading.postValue(false)
                        }
                    })
                },
                { error -> viewModel.error.postValue(error) }
            )
        }
    }

    suspend fun addUser(
        activity: Activity,
        user: UserRequest,
        onSuccess: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            isNetworkConnected(
                activity,
                {
                    service.postUser(user).safeSubscribe(object : Observer<User> {
                        override fun onSubscribe(d: Disposable) {
                            viewModel.loading.postValue(true)
                        }

                        override fun onNext(t: User) {
                            AppDatabase.databaseWriteExecutor.execute {
                                db.userDao().insertAll(t)
                                activity.runOnUiThread { onSuccess() }
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewModel.error.postValue(e.message)
                        }

                        override fun onComplete() {
                            viewModel.loading.postValue(false)
                        }
                    })
                },
                { error -> viewModel.error.postValue(error) }
            )
        }
    }

    suspend fun updateUser(
        activity: Activity,
        id: Int,
        user: UserRequest,
        onSuccess: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            isNetworkConnected(
                activity,
                {
                    service.putUser(id, user).safeSubscribe(object : Observer<User> {
                        override fun onSubscribe(d: Disposable) {
                            viewModel.loading.postValue(true)
                        }

                        override fun onNext(t: User) {
                            AppDatabase.databaseWriteExecutor.execute {
                                t.id = id
                                db.userDao().insertAll(t)
                                viewModel.user.postValue(t)
                                activity.runOnUiThread { onSuccess() }
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewModel.error.postValue(e.message)
                        }

                        override fun onComplete() {
                            viewModel.loading.postValue(false)
                        }
                    })
                },
                { error -> viewModel.error.postValue(error) }
            )
        }
    }

    suspend fun deleteUser(
        activity: Activity,
        id: Int,
        onSuccess: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            isNetworkConnected(
                activity,
                {
                    service.deleteUser(id).safeSubscribe(object : Observer<retrofit2.Response<Void>> {
                        override fun onSubscribe(d: Disposable) {
                            viewModel.loading.postValue(true)
                        }

                        override fun onNext(t: retrofit2.Response<Void>) {
                            AppDatabase.databaseWriteExecutor.execute {
                                db.userDao().delete(id)
                                viewModel.user.postValue(null)
                                activity.runOnUiThread { onSuccess() }
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewModel.error.postValue(e.message)
                        }

                        override fun onComplete() {
                            viewModel.loading.postValue(false)
                        }
                    })
                },
                { error -> viewModel.error.postValue(error) }
            )
        }
    }

    companion object {
        private fun isNetworkConnected(
            activity: Activity,
            connected : () -> Unit,
            error: (message: String) -> Unit
        ) {
            val cm =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected) {
                connected()
            } else {
                error(activity.getString(R.string.message_no_internet_connection))
            }
        }
    }
}