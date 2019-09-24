package com.example.bradmoeller.myapplication.clean.data.model

data class User(
        val id: Int,
        val name: String,
        val username: String,
        val email: String,
        val company: Company) {

//    object Mapper {
//        fun from(from: UserDto) {
//            from.users.forEach {
//                User(it.id, it.name, it.username, it.email, it.company)
//            }
//        }
//    }
}