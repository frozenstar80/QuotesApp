package com.example.quotesapp.domain.api

import com.example.quotesapp.data.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body map : HashMap<String,String>
    ):LoginResponse

    @POST("api/auth/sign-up")
    suspend fun signUpUser(
        @Body map : HashMap<String,String>
    ): SignUpResponse

    @Multipart
    @POST("api/user/upload/image")
    suspend fun uploadUserFiles(
        @Part image: MultipartBody.Part?
    ): UploadUserFileResponse

    @POST("api/user/create-profile/{id}")
    suspend fun createProfile(
        @Body map : HashMap<String,Any?>,
        @Path("id") id:String
    ): CreateProfileResponse

    @GET("api/user/show")
    suspend fun showOwnProfile(): ShowOwnProfileResponse

    @GET("api/search/recent")
    suspend fun recentSearchApi(@Header("Authorization") token:String): RecentSearchResponse

    @GET("api/user/view-profile/{id}/true")
    suspend fun showOtherUserProfile(@Path("id") id:String,@Header("Authorization") token:String): RecentSearchResponse

    @GET("api/search/show/{name}")
    suspend fun searchUser(@Path("name") name:String,@Header("Authorization") token:String): SearchUserResponse
}