package io.github.jwgibanez.contacts

import io.github.jwgibanez.contacts.database.AppDatabase
import io.github.jwgibanez.contacts.service.RqresService
import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val database: AppDatabase,
    private val service: RqresService
) {

    suspend fun fetchUsers() {
        withContext(Dispatchers.IO) {
            service.getUsers().safeSubscribe(object : Observer<Response<User>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(value: Response<User>) {
                    value.data
                }

                override fun onError(e: Throwable) {
                    e.localizedMessage
                }

                override fun onComplete() {

                }
            })
        }
    }
}