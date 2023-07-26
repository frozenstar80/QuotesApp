package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.data.UploadUserFileResponse
import com.example.quotesapp.domain.repository.DataRepository
import com.example.quotesapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class UploadDataViewModel @Inject constructor(
        private val dataRepository: DataRepository
    ) : ViewModel() {
        private val _saveFileDataResponse: MutableLiveData<StatusMessageResponse> = MutableLiveData()
        val saveFileDataResponse get() = _saveFileDataResponse

        fun saveFile(map: HashMap<String,String>, token:String,id:String){
            viewModelScope.launch {
                dataRepository.saveFile(map,token,id).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _saveFileDataResponse.value = response
                }
            }
        }

    private val _saveExchangeFileDataResponse: MutableLiveData<Event<StatusMessageResponse>> = MutableLiveData()
    val saveExchangeFileDataResponse get() = _saveExchangeFileDataResponse

    fun saveExchangeFile(map: HashMap<String,String>,token:String,fileType:String,userType:String,id:String){
        viewModelScope.launch {
            dataRepository.saveExchangeFiles(map,id,fileType,userType,token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _saveExchangeFileDataResponse.value = Event(response)
            }
        }
    }

    fun saveQuote(map: HashMap<String,String>, token:String,id: String,userType:String){
        viewModelScope.launch {
            dataRepository.saveQuote(map,token,id, userType).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _saveFileDataResponse.value = response
            }
        }
    }

    private val _updateVideoImageDocument: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val updateVideoImageDocument get() = _updateVideoImageDocument

    fun updateData(map: HashMap<String,String>, token:String,id:String){
        viewModelScope.launch {
            dataRepository.updateFile(map,token,id).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _updateVideoImageDocument.value = response
            }
        }
    }

    private val _updateQuote: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val updateQuote get() = _updateQuote

    fun updateQuote(map: HashMap<String,String>, token:String,id: String){
        viewModelScope.launch {
            dataRepository.updateQuote(map,token,id).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _updateQuote.value = response
            }
        }
    }


    private val _deleteVideoImageDocument: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val deleteVideoImageDocument get() = _deleteVideoImageDocument

    fun deleteData(token:String,id:String){
        viewModelScope.launch {
            dataRepository.deleteFile(token,id).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _deleteVideoImageDocument.value = response
            }
        }
    }

    private val _deleteQuote: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val deleteQuote get() = _deleteQuote

    fun deleteQuote( token:String,id: String){
        viewModelScope.launch {
            dataRepository.deleteQuote(token,id).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _deleteQuote.value = response
            }
        }
    }



    private val _uploadFileLiveData: MutableLiveData<UploadUserFileResponse> = MutableLiveData()
        val uploadFileLiveData get() = _uploadFileLiveData

        fun uploadUserFile(image: MultipartBody.Part?,file:String){
            viewModelScope.launch {
                dataRepository.uploadUserFiles(image,file).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _uploadFileLiveData.value = response
                }
            }
        }

    fun uploadUserFile(image: MultipartBody.Part?,file:String,token: String){
        viewModelScope.launch {
            dataRepository.uploadMemberPlusFiles(image,file,token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _uploadFileLiveData.value = response
            }
        }
    }

    private val _uploadExchangeFileLiveData: MutableLiveData<UploadUserFileResponse> = MutableLiveData()
    val uploadExchangeFileLiveData get() = _uploadExchangeFileLiveData

    fun uploadExchangeUserFile(image: MultipartBody.Part?,file:String,token: String){
        viewModelScope.launch {
            dataRepository.uploadExchangeFiles(image,file,token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _uploadExchangeFileLiveData.value = response
            }
        }
    }


    }
