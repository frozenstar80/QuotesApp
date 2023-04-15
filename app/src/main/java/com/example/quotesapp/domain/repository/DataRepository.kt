package com.example.quotesapp.domain.repository
import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.SignUpResponse
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

}