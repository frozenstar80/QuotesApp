package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.OtherUserProfileResponse
import com.example.quotesapp.data.RecentSearchResponse
import com.example.quotesapp.data.SearchUserResponse
import com.example.quotesapp.domain.repository.DataRepository
import com.example.quotesapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPeopleViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _postLiveData: MutableLiveData<SearchUserResponse> = MutableLiveData()
    val postLiveData get() = _postLiveData
    private val _recentSearchLiveData: MutableLiveData<RecentSearchResponse> = MutableLiveData()
    val recentSearchLiveData get() = _recentSearchLiveData
    private val _otherUserProfileLiveData: MutableLiveData<Event<OtherUserProfileResponse>> = MutableLiveData()
    val otherUserProfileLiveData get() = _otherUserProfileLiveData

    fun searchPeople(keyword:String,token:String){
        viewModelScope.launch {
            dataRepository.searchUser(keyword,token).catch { e->
                Log.d("login", "login: ${e.message}")
            }.collect{response->
                _postLiveData.value = response
            }
        }
    }
    fun recentUsers(token:String){
        viewModelScope.launch {
            dataRepository.recentSearchedUser(token).catch { e->
                Log.d("login", "login: ${e.message}")
            }.collect{response->
                recentSearchLiveData.value = response
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