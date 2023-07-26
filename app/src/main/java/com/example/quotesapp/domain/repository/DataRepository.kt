package com.example.quotesapp.domain.repository

import com.example.quotesapp.data.BurialResponse
import com.example.quotesapp.data.CreateProfileResponse
import com.example.quotesapp.data.ExchangeFileResponse
import com.example.quotesapp.data.FileDataResponse
import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.NotificationCount
import com.example.quotesapp.data.NotificationResponse
import com.example.quotesapp.data.OtherUserProfileResponse
import com.example.quotesapp.data.RecentSearchResponse
import com.example.quotesapp.data.SearchUserResponse
import com.example.quotesapp.data.ShowOwnProfileResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.data.UploadUserFileResponse
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

    suspend fun getSignUpData(map: HashMap<String,String>): Flow<StatusMessageResponse> = flow {
        val response = apiService.signUpUser(map )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun createProfile(map: HashMap<String,Any?>,id:String): Flow<CreateProfileResponse> = flow {
        val response = apiService.createProfile(map,id )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun createMemberProfile(map: HashMap<String,Any?>,token:String): Flow<CreateProfileResponse> = flow {
        val response = apiService.createMemberProfile(map,authorization+token )
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

    suspend fun showOtherUserProfile(id:String,token:String): Flow<OtherUserProfileResponse> = flow {
        val response = apiService.showOtherUserProfile(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showOwnProfile(token:String): Flow<ShowOwnProfileResponse> = flow {
        val response = apiService.showOwnProfile(authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun uploadUserFiles(image: MultipartBody.Part?,file:String): Flow<UploadUserFileResponse> = flow {
        val response = apiService.uploadUserFiles(file,image )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun sendFollowRequest(id:String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.sendFollowRequest(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun getNotificationCount(token:String): Flow<NotificationCount> = flow {
        val response = apiService.notificationsCount(authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun updateNotification(token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.updateNotificationCount(authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showNotification(token:String): Flow<NotificationResponse> = flow {
        val response = apiService.notifications(authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun acceptFollowRequest(id:String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.acceptRequest(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun denyFollowRequest(id:String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.rejectRequest(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun saveFile(map: HashMap<String,String>,token:String,id: String): Flow<StatusMessageResponse> = flow {
        val response = apiService.saveFile(map,authorization+token,id )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showAllFileForAType(type:String,id:String,token:String,userType:String): Flow<FileDataResponse> = flow {
        val response = apiService.showAllFileForAType(type,id,authorization+token ,userType)
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun saveQuote(map: HashMap<String,String>,token:String,id:String,userType: String): Flow<StatusMessageResponse> = flow {
        val response = apiService.saveQuote(map,authorization+token ,userType, id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showAllQuotes(token:String,id:String,userType: String): Flow<FileDataResponse> = flow {
        val response = apiService.showAllQuotes(authorization+token,userType, id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun updateQuote(map: HashMap<String,String>,token:String,id:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.updateQuote(map,authorization+token ,id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun deleteQuote(token:String,id:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.deleteQuote(authorization+token, id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun updateFile(map: HashMap<String,String>,token:String,id:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.updateFile(map,authorization+token ,id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun deleteFile(token:String,id:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.deleteFile(authorization+token, id)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun updateMemberProfile(map: HashMap<String,Any?>,token:String): Flow<CreateProfileResponse> = flow {
        val response = apiService.updateMemberProfile(map,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun uploadMemberPlusFiles(image: MultipartBody.Part?,file:String,token:String): Flow<UploadUserFileResponse> = flow {
        val response = apiService.uploadMemberFiles(file,image,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun unFollowUser(id:String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.unFollowUser(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun removeUser(id:String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.removeUser(id,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowing(id:String,token:String): Flow<SearchUserResponse> = flow {
        val response = apiService.showAllFollowing(authorization+token,id )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun getFollowers(id:String,token:String): Flow<SearchUserResponse> = flow {
        val response = apiService.showAllFollowers(authorization+token,id )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun uploadExchangeFiles(image: MultipartBody.Part?,file:String,token:String): Flow<UploadUserFileResponse> = flow {
        val response = apiService.uploadExchangeItem(file,image,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun saveExchangeFiles(map: HashMap<String,String>,id:String,fileType:String,userType: String,token:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.saveExchange(map,id,fileType,userType,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun sentExchangeItem(userType:String,token:String): Flow<ExchangeFileResponse> = flow {
        val response = apiService.getSentExchangeItem(userType,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun receivedExchangeItem(userType:String,token:String): Flow<ExchangeFileResponse> = flow {
        val response = apiService.getReceivedExchangeItem(userType,authorization+token )
        emit(response)
    }.flowOn(Dispatchers.IO)


    suspend fun saveBurial(map: HashMap<String,String>,token:String,id: String,itemType:String,userType:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.addBurial(map,authorization+token,id ,itemType, userType)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun showAllBurialData(id:String,token:String,itemType:String,tab:String): Flow<BurialResponse> = flow {
        val response = apiService.getBurial(authorization+token ,id, itemType ,tab)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun updateBurial(map: HashMap<String,String>,token:String,id:String,itemType:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.updateBurial(map,authorization+token ,id,itemType)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun deleteBurial(token:String,id:String,itemType:String): Flow<StatusMessageResponse> = flow {
        val response = apiService.deleteBurial(authorization+token, id,itemType)
        emit(response)
    }.flowOn(Dispatchers.IO)




}