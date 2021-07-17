package io.github.jwgibanez.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jwgibanez.contacts.Repository
import io.github.jwgibanez.contacts.database.AppDatabase
import io.github.jwgibanez.contacts.service.RqresService
import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    database: AppDatabase,
    service: RqresService
) : ViewModel() {

    private val repository = Repository(database, service)

    fun fetchUsers() {
        viewModelScope.launch {
            repository.fetchUsers()
        }
    }
}