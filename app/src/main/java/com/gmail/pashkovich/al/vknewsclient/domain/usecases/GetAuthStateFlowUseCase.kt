package com.gmail.pashkovich.al.vknewsclient.domain.usecases

import com.gmail.pashkovich.al.vknewsclient.domain.entity.AuthState
import com.gmail.pashkovich.al.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateFlowUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<AuthState> {
       return repository.getAuthStateFlow()
    }
}