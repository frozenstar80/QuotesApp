package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _postLiveData: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val postLiveData get() = _postLiveData


    fun signInUser(map: HashMap<String,String>){
        viewModelScope.launch {
            dataRepository.getSignUpData(map).catch { e->
                Log.d("register", "register: ${e.message}")
            }.collect{response->
                _postLiveData.value = response
            }
        }
    }

    private val _loginLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginLiveData get() = _loginLiveData

    fun loginUser(map: HashMap<String,String>){
        viewModelScope.launch {
            dataRepository.getLoginData(map).catch { e->
                Log.d("login", "login: ${e.message}")
            }.collect{response->
                _loginLiveData.value = response
            }
        }
    }
}