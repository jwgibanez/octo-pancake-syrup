package io.github.jwgibanez.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jwgibanez.contacts.repository.Repository
import io.github.jwgibanez.contacts.database.AppDatabase
import io.github.jwgibanez.contacts.service.RqresService
import io.github.jwgibanez.contacts.service.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.LiveData

import android.app.Activity


@HiltViewModel
class ContactsViewModel @Inject constructor(
    database: AppDatabase,
    service: RqresService
) : ViewModel() {

    private val repository = Repository(this, database, service)

    var users: LiveData<List<User>> = database.userDao().all()
    var error = MutableLiveData<String?>(null)
    var loading = MutableLiveData(false)

    override fun onCleared() {
        super.onCleared()
        error.value = null
        loading.value = false
    }

    fun fetchUsers(activity: Activity) {
        viewModelScope.launch {
            repository.fetchUsers(activity)
        }
    }
}