package com.example.quotesapp.domain.repository
import com.example.quotesapp.data.*
import com.example.quotesapp.domain.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject


class DataRepository @Inject constructor(private val apiService: ApiService) {

    var authorization = "Bearer "

    suspend fun getLoginData(map : HashMap<String,String>): Flow<LoginResponse> = flow {
        val response = apiService.loginUser(map)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun getSignUpData(map: HashMap<String,String>): Flow<SignUpResponse> = flow {
        val response = apiService.signUpUser(map )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun createProfile(map: HashMap<String,Any?>,id:String): Flow<CreateProfileResponse> = flow {
        val response = apiService.createProfile(map,id )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun searchUser(name:String,token:String): Flow<SearchUserResponse> = flow {
        val response = apiService.searchUser(name,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun recentSearchedUser(token:String): Flow<RecentSearchResponse> = flow {
        val response = apiService.recentSearchApi(authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showOwnProfile(): Flow<ShowOwnProfileResponse> = flow {
        val response = apiService.showOwnProfile()
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun uploadUserFiles(image: MultipartBody.Part?,file:String): Flow<UploadUserFileResponse> = flow {
        val response = apiService.uploadUserFiles(image )
        emit(response)
    }.flowOn(Dispatchers.IO)


}