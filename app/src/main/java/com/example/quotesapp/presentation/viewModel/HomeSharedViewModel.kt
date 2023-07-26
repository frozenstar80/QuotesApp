package com.example.quotesapp.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeSharedViewModel : ViewModel() {

    val isDataChange= MutableLiveData<Boolean>(false)
}