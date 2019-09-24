package com.example.bradmoeller.myapplication.clean.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.bradmoeller.myapplication.clean.data.UserApi
import com.example.bradmoeller.myapplication.clean.data.model.Result
import com.example.bradmoeller.myapplication.clean.domain.UserRepository
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit


class CleanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()
        val userApi = retrofit.create(UserApi::class.java)
        val repo = UserRepository(userApi)

        launch {
            val result = repo.getUsers()
            when(result) {
                is Result.Success -> {
                    Log.e("result", "Success")
                }
                is Result.Error -> {
                    Log.e("result", "Error")
                }
                is Result.Exception -> {
                    Log.e("result", "Exception")
                }
            }
        }


    }
}