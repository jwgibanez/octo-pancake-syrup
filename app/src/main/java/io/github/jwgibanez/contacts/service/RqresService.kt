package io.github.jwgibanez.contacts.service

import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.reactivex.Observable
import retrofit2.http.GET

interface RqresService {

    @GET("users?per_page=10")
    fun getUsers() : Observable<Response<User>>

    companion object {
        const val API_HOST = "https://reqres.in/api/"
    }
}