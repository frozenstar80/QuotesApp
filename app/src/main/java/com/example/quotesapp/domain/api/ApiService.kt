package com.example.quotesapp.domain.api

import com.example.quotesapp.data.*
import retrofit2.http.Body
import retrofit2.http.GET
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

    @POST("api/user/upload/{file Type}")
    suspend fun uploadUserFiles(
        @Body map : HashMap<String,String>
    ): UploadUserFileResponse

    @POST("api/user/create-profile/{id}")
    suspend fun createProfile(
        @Body map : HashMap<String,String>
    ): CreateProfileResponse

    @GET("api/user/create-profile/{id}")
    suspend fun showOwnProfile(
        @Body map : HashMap<String,String>
    ): ShowOwnProfileResponse


    @GET("api/user/create-profile/{id}")
    suspend fun searchUser(
        @Body map : HashMap<String,String>
    ): SearchUserResponse
}