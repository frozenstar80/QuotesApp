package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.CreateProfileResponse
import com.example.quotesapp.data.UploadUserFileResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadUserFileViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _postLiveData: MutableLiveData<UploadUserFileResponse> = MutableLiveData()
    val postLiveData get() = _postLiveData

    fun uploadUserFile(map: HashMap<String,String>){
        viewModelScope.launch {
            dataRepository.uploadUserFiles(map).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _postLiveData.value = response
            }
        }
    }
}
