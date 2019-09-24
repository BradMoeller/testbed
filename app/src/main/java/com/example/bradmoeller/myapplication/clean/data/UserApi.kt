package com.example.bradmoeller.myapplication.clean.data

import com.example.bradmoeller.myapplication.clean.data.model.User
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("users")
    fun fetchUsers(): Deferred<Response<List<User>>>
}