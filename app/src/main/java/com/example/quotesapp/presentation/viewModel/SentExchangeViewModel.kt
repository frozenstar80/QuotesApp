package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.ExchangeFileResponse
import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.SearchUserResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentExchangeViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _postLiveData: MutableLiveData<ExchangeFileResponse> = MutableLiveData()
    val postLiveData get() = _postLiveData

    fun getSentItems(userType: String, token: String) {
        viewModelScope.launch {
            dataRepository.sentExchangeItem(userType, token).catch { e ->
                Log.d("login", "login: ${e.message}")
            }.collect { response ->
                _postLiveData.value = response
            }
        }
    }
}