package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.BurialResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BurialViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _getBurialLiveData: MutableLiveData<BurialResponse> = MutableLiveData()
    val getBurialLiveData get() = _getBurialLiveData

    fun getBurial(id:String,token:String,itemType:String,tab:String){
        viewModelScope.launch {
            dataRepository.showAllBurialData(id, token, itemType, tab).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _getBurialLiveData.value = response
            }
        }
    }
}
