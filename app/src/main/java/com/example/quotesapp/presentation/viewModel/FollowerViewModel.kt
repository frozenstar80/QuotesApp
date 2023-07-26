package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.LoginResponse
import com.example.quotesapp.data.OtherUserProfileResponse
import com.example.quotesapp.data.SearchUserResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.domain.repository.DataRepository
import com.example.quotesapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _postLiveData: MutableLiveData<SearchUserResponse> = MutableLiveData()
    val postLiveData get() = _postLiveData
    private val _removeLiveData: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val removeLiveData get() = _removeLiveData
    private val _otherUserProfileLiveData: MutableLiveData<Event<OtherUserProfileResponse>> = MutableLiveData()
    val otherUserProfileLiveData get() = _otherUserProfileLiveData

    fun getFollowers(id:String,token:String){
        viewModelScope.launch {
            dataRepository.getFollowers(id, token).catch { e->
                Log.d("login", "login: ${e.message}")
            }.collect{response->
                _postLiveData.value = response
            }
        }
    }

    fun removeUser(id:String,token:String){
        viewModelScope.launch {
            dataRepository.removeUser(id,token).catch {e->
                Log.d("login", "login: ${e.message}")
            }.collect{
                _removeLiveData.value =  it
            }
        }
    }

    fun showUserDetails(id:String,token:String){
        viewModelScope.launch {
            dataRepository.showOtherUserProfile(id,token).catch { e->
                Log.d("login", "login: ${e.message}")
            }.collect{response->
                _otherUserProfileLiveData.value = Event(response)
            }
        }
    }
}