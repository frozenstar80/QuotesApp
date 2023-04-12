package com.example.quotesapp.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedLaunchScreenViewModel : ViewModel() {
    val isLaunchPageOpened = MutableLiveData<Boolean>(true)
}