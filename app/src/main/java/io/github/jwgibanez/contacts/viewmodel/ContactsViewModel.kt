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

import android.app.Activity
import io.github.jwgibanez.contacts.service.request.UserRequest


@HiltViewModel
class ContactsViewModel @Inject constructor(
    database: AppDatabase,
    service: RqresService
) : ViewModel() {

    private val repository = Repository(this, database, service)

    val users = database.userDao().all()
    val user = MutableLiveData<User?>(null)
    val error = MutableLiveData<String?>(null)
    val loading = MutableLiveData(false)

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

    fun addUser(
        activity: Activity,
        user: UserRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.addUser(activity, user, onSuccess)
        }
    }

    fun updateUser(
        activity: Activity,
        id: Int,
        user: UserRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.updateUser(activity, id, user, onSuccess)
        }
    }

    fun deleteUser(
        activity: Activity,
        id: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.deleteUser(activity, id, onSuccess)
        }
    }
}