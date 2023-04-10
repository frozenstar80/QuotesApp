package com.example.quotesapp.domain.repository
import com.example.quotesapp.domain.api.ApiService
import javax.inject.Inject


class DataRepository
@Inject constructor(private val apiService: ApiService) {

//    suspend fun getData(): Flow<DataResponse> = flow {
//        val response = apiService.getMemes()
//        emit(response)
//    }.flowOn(Dispatchers.IO)

}