package com.air_wheelly.wheelly.util

import com.air_wheelly.wheelly.data.IAuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: IAuthApiService by lazy {
        retrofit.create(IAuthApiService::class.java)
    }
}