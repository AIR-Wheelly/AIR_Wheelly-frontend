package com.air_wheelly.wheelly.model

interface IUser {
    fun getEmail(): String?
    fun getPassword(): String?
    fun loginStatus(): Int
}
