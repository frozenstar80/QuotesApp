package com.example.quotesapp.domain.repository
import com.example.quotesapp.data.*
import com.example.quotesapp.domain.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DataRepository
@Inject constructor(private val apiService: ApiService) {

    suspend fun getLoginData(map : HashMap<String,String>): Flow<LoginResponse> = flow {
        val response = apiService.loginUser(map)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun getSignUpData(map: HashMap<String,String>): Flow<SignUpResponse> = flow {
        val response = apiService.signUpUser(map )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun createProfile(map: HashMap<String,String>): Flow<CreateProfileResponse> = flow {
        val response = apiService.createProfile(map )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun searchUser(map: HashMap<String,String>): Flow<SearchUserResponse> = flow {
        val response = apiService.searchUser(map )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showOwnProfile(map: HashMap<String,String>): Flow<ShowOwnProfileResponse> = flow {
        val response = apiService.showOwnProfile(map )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun uploadUserFiles(map: HashMap<String,String>): Flow<UploadUserFileResponse> = flow {
        val response = apiService.uploadUserFiles(map )
        emit(response)
    }.flowOn(Dispatchers.IO)


}