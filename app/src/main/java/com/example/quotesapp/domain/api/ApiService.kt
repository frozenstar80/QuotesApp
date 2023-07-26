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
    ): StatusMessageResponse

    @Multipart
    @POST("api/member-plus/upload/{fileType}")
    suspend fun uploadMemberFiles(
        @Path("fileType") fileType:String,
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token:String
    ): UploadUserFileResponse

    @Multipart
    @POST("api/user/upload/{fileType}")
    suspend fun uploadUserFiles(
        @Path("fileType") fileType:String,
        @Part image: MultipartBody.Part?
    ): UploadUserFileResponse

    @POST("api/user/create-profile/{id}")
    suspend fun createProfile(
        @Body map : HashMap<String,Any?>,
        @Path("id") id:String
    ): CreateProfileResponse

    @PUT("api/member-plus/update")
    suspend fun updateMemberProfile(
        @Body map : HashMap<String,Any?>,
        @Header("Authorization") token:String
    ): CreateProfileResponse

    @POST("api/member-plus/create-profile")
    suspend fun createMemberProfile(
        @Body map : HashMap<String,Any?>,
        @Header("Authorization") token:String
    ): CreateProfileResponse

    @GET("api/user/show")
    suspend fun showOwnProfile(@Header("Authorization") token:String): ShowOwnProfileResponse

    @GET("api/search/recent")
    suspend fun recentSearchApi(@Header("Authorization") token:String): RecentSearchResponse

    @GET("api/user/view-profile/{id}/true")
    suspend fun showOtherUserProfile(@Path("id") id:String,@Header("Authorization") token:String): OtherUserProfileResponse

    @GET("api/search/show/{name}")
    suspend fun searchUser(@Path("name") name:String,@Header("Authorization") token:String): SearchUserResponse

    @POST("api/follow/request/{id}")
    suspend fun sendFollowRequest(@Path("id") id:String,@Header("Authorization") token:String): StatusMessageResponse

    @PUT("api/follow/accept/{id}")
    suspend fun acceptRequest(@Path("id") id:String,@Header("Authorization") token:String): StatusMessageResponse

    @DELETE("api/follow/reject/{id}")
    suspend fun rejectRequest(@Path("id") id:String,@Header("Authorization") token:String): StatusMessageResponse


    @GET("api/notifications/recent")
    suspend fun notifications(@Header("Authorization") token:String): NotificationResponse

    @GET("api/notifications")
    suspend fun notificationsCount(@Header("Authorization") token:String): NotificationCount

    @PUT("api/notifications/update")
    suspend fun updateNotificationCount(@Header("Authorization") token:String): StatusMessageResponse

    @POST("api/file/save/{id}")
    suspend fun saveFile( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("id") id : String): StatusMessageResponse

    @GET("api/file/show/{fileType}/{id}/{userType}")
    suspend fun showAllFileForAType(@Path("fileType") type:String,
                                    @Path("id") id: String,
                                    @Header("Authorization") token:String,
                                    @Path("userType") userType:String
    ): FileDataResponse

    @POST("api/quote/create/{userType}/{id}")
    suspend fun saveQuote( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("userType") userType: String,@Path("id") id:String): StatusMessageResponse

    @GET("api/quote/show/{userType}/{id}")
    suspend fun showAllQuotes(@Header("Authorization") token:String,@Path("userType") userType: String,@Path("id") id:String): FileDataResponse

    @POST("api/quote/update/{quoteId}")
    suspend fun updateQuote( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("quoteId") id:String): StatusMessageResponse

    @DELETE("api/quote/delete/{quoteId}")
    suspend fun deleteQuote( @Header("Authorization") token:String,@Path("quoteId") id:String): StatusMessageResponse

    @PUT("api/file/update/{fileId}")
    suspend fun updateFile( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("fileId") id:String): StatusMessageResponse

    @DELETE("api/file/delete/{fileId}")
    suspend fun deleteFile( @Header("Authorization") token:String,@Path("fileId") id:String): StatusMessageResponse

    @GET("api/user/following/{id}")
    suspend fun showAllFollowing(@Header("Authorization") token:String,@Path("id") id:String): SearchUserResponse

    @GET("api/user/followers/{id}")
    suspend fun showAllFollowers(@Header("Authorization") token:String,@Path("id") id:String): SearchUserResponse

    @DELETE("api/follow/un-follow/{id}")
    suspend fun unFollowUser(@Path("id") id:String,@Header("Authorization") token:String): StatusMessageResponse

    @DELETE("api/follow/remove/{id}")
    suspend fun removeUser(@Path("id") id:String,@Header("Authorization") token:String): StatusMessageResponse

    @Multipart
    @POST("api/exchange/upload-item/{fileType}")
    suspend fun uploadExchangeItem(@Path("fileType") fileType:String,
                                   @Part image: MultipartBody.Part?,
                                   @Header("Authorization") token:String): UploadUserFileResponse

    @POST("api/exchange/item/{id}/{fileType}/{userType}")
    suspend fun saveExchange(
        @Body map : HashMap<String,String>,
        @Path("id") id:String,
        @Path("fileType") fileType:String,
        @Path("userType") userType:String,
        @Header("Authorization") token:String)
    : StatusMessageResponse

    @GET("api/exchange/sent/{userType}")
    suspend fun getSentExchangeItem(@Path("userType") userType:String,@Header("Authorization") token:String):ExchangeFileResponse

    @GET("api/exchange/received/{userType}")
    suspend fun getReceivedExchangeItem(@Path("userType") userType:String, @Header("Authorization") token:String):ExchangeFileResponse

    @POST("api/{itemType}/{id}/{userType}")
    suspend fun addBurial( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("id") id:String,@Path("itemType") itemType:String,@Path("userType") userType:String): StatusMessageResponse

    @GET("api/{itemType}/{id}/{tab}")
    suspend fun getBurial( @Header("Authorization") token:String,@Path("id") id:String,@Path("itemType") itemType:String,@Path("tab") tab:String): BurialResponse

    @PUT("api/{itemType}/{id}")
    suspend fun updateBurial( @Body map : HashMap<String,String>,@Header("Authorization") token:String,@Path("id") id:String,@Path("itemType") itemType:String): StatusMessageResponse

    @DELETE("api/{itemType}/{id}")
    suspend fun deleteBurial( @Header("Authorization") token:String,@Path("id") id:String,@Path("itemType") itemType:String): StatusMessageResponse




}