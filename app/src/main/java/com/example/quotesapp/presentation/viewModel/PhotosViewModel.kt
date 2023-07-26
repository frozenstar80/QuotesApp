package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.FileDataResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotosViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
        private val _getDocumentsLiveData: MutableLiveData<FileDataResponse> = MutableLiveData()
        val getDocumentsLiveData get() = _getDocumentsLiveData

        fun getDocuments(type:String,id:String,token:String,userType:String){
            viewModelScope.launch {
                dataRepository.showAllFileForAType(type, id, token,userType).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _getDocumentsLiveData.value = response
                }
            }
        }
    }
