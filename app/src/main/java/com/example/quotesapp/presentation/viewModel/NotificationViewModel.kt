package com.example.quotesapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.NotificationCount
import com.example.quotesapp.data.NotificationResponse
import com.example.quotesapp.data.StatusMessageResponse
import com.example.quotesapp.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
    class NotificationViewModel @Inject constructor(
        private val dataRepository: DataRepository
    ) : ViewModel() {
        private val _notificationResonse: MutableLiveData<NotificationResponse> = MutableLiveData()
        val notificationResonse get() = _notificationResonse

        fun showNotification(token: String){
            viewModelScope.launch {
                dataRepository.showNotification(token).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _notificationResonse.value = response
                }
            }
        }

    private val _getNotificationCountResponse: MutableLiveData<NotificationCount> = MutableLiveData()
    val getNotificationCountResponse get() = _getNotificationCountResponse

    fun showNotificationCount(token: String){
        viewModelScope.launch {
            dataRepository.getNotificationCount(token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _getNotificationCountResponse.value = response
            }
        }
    }

    private val _updateNotificationCountResponse: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val updateNotificationCountResponse get() = _updateNotificationCountResponse

    fun updateNotificationCount(token: String){
        viewModelScope.launch {
            dataRepository.updateNotification(token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _updateNotificationCountResponse.value = response
            }
        }
    }
        private val _acceptRequest: MutableLiveData<StatusMessageResponse> = MutableLiveData()
        val acceptRequest get() = _acceptRequest

        fun acceptRequest(id: String,token:String){
            viewModelScope.launch {
                dataRepository.acceptFollowRequest(id,token).catch { e->
                    Log.d("data", "endpoint: ${e.message}")
                }.collect{response->
                    _acceptRequest.value = response
                }
            }
        }


    private val _denyRequest: MutableLiveData<StatusMessageResponse> = MutableLiveData()
    val denyRequest get() = _denyRequest

    fun denyRequest(id: String,token:String){
        viewModelScope.launch {
            dataRepository.denyFollowRequest(id,token).catch { e->
                Log.d("data", "endpoint: ${e.message}")
            }.collect{response->
                _denyRequest.value = response
            }
        }
    }
    }
