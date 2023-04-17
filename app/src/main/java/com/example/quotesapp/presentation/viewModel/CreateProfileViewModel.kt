package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.CreateProfileResponse
import com.example.quotesapp.data.UploadUserFileResponse
import com.example.quotesapp.domain.repository.DataRepository
import com.example.quotesapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


    @HiltViewModel
    class CreateProfileViewModel @Inject constructor(
        private val dataRepository: DataRepository
    ) : ViewModel() {
        private val _createProfileLiveData: MutableLiveData<CreateProfileResponse> = MutableLiveData()
        val createProfileLiveData get() = _createProfileLiveData

        fun createProfile(map: HashMap<String,Any?>,id:String){
            viewModelScope.launch {
                dataRepository.createProfile(map,id).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _createProfileLiveData.value = response
                }
            }
        }

        private val _uploadPhotoLiveData: MutableLiveData<UploadUserFileResponse> = MutableLiveData()
        val uploadPhotoLiveData get() = _uploadPhotoLiveData

        fun uploadUserFile(image: MultipartBody.Part?){
            viewModelScope.launch {
                dataRepository.uploadUserFiles(image,"image").catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _uploadPhotoLiveData.value = response
                }
            }
        }
    }
