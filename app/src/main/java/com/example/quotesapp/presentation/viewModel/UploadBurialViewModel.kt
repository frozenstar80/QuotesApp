package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.data.UploadUserFileResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadBurialViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _addBurialLiveData: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val addBurialLiveData get() = _addBurialLiveData
    private val _updateBurialLiveData: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val updateBurialLiveData get() = _updateBurialLiveData
    private val _deleteBurialLiveData: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val deleteBurialLiveData get() = _deleteBurialLiveData

    fun addBurial(map: HashMap<String,String>,token:String,id: String,itemType:String,userType:String){
        viewModelScope.launch {
            dataRepository.saveBurial(map, token, id, itemType, userType).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _addBurialLiveData.value = response
            }
        }
    }
    fun deleteBurial(token:String,id:String,itemType:String){
        viewModelScope.launch {
            dataRepository.deleteBurial(token,id,itemType).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _addBurialLiveData.value = response
            }
        }
    }
    fun updateBurial(map: HashMap<String,String>,token:String,id:String,itemType:String){
        viewModelScope.launch {
            dataRepository.updateBurial(map,token,id,itemType).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _addBurialLiveData.value = response
            }
        }
    }

    private val _uploadFileLiveData: MutableLiveData<UploadUserFileResponse> = MutableLiveData()
    val uploadFileLiveData get() = _uploadFileLiveData

    fun uploadUserFile(image: MultipartBody.Part?, file:String){
        viewModelScope.launch {
            dataRepository.uploadUserFiles(image,file).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _uploadFileLiveData.value = response
            }
        }
    }
}
