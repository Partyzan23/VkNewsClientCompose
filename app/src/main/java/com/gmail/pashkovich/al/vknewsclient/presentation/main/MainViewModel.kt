package com.gmail.pashkovich.al.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.CheckAuthStateUseCase
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {

    val authState = getAuthStateFlowUseCase()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}