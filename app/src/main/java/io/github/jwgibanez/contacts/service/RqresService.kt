package io.github.jwgibanez.contacts.service

import io.github.jwgibanez.contacts.service.model.Response
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.service.request.UserRequest
import io.reactivex.Observable
import retrofit2.http.*

interface RqresService {

    @GET("users?per_page=10")
    fun getUsers() : Observable<Response<User>>

    @POST("users")
    fun postUser(@Body user: UserRequest) : Observable<User>

    @PUT("users/{id}")
    fun putUser(
        @Path("id") id: Int,
        @Body user: UserRequest
    ) : Observable<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int) : Observable<retrofit2.Response<Void>>

    companion object {
        const val API_HOST = "https://reqres.in/api/"
    }
}