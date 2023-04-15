package com.example.quotesapp.domain.api

import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body map : HashMap<String,String>
    ):LoginResponse

    @POST("api/auth/sign-up")
    suspend fun signUpUser(
        @Body map : HashMap<String,String>
    ): SignUpResponse

}