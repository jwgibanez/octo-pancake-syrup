package io.github.jwgibanez.contacts.repository

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.database.AppDatabase
import io.github.jwgibanez.contacts.service.RqresService
import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
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
                            AppDatabase.databaseWriteExecutor.execute {
                                db.userDao().insertAll(value.data)
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