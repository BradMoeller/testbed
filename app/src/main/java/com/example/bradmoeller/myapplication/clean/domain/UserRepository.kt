package com.example.bradmoeller.myapplication.clean.domain

import com.example.bradmoeller.myapplication.clean.data.UserApi
import com.example.bradmoeller.myapplication.clean.data.model.Result
import com.example.bradmoeller.myapplication.clean.data.model.User

class UserRepository constructor(private val userApi: UserApi){

    suspend fun getUsers() : Result<List<User>> {

        return try {
            val result = userApi.fetchUsers().await()

            if (result.isSuccessful) {
                Result.Success(result.body() ?: listOf())
            } else {
                Result.Error(1)
            }
        } catch (e: UnsupportedOperationException) {
            e.printStackTrace()
            Result.Exception(e)
        }
    }
}

