package com.gmail.pashkovich.al.vknewsclient.domain.usecases

import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<List<FeedPost>> {
       return repository.getRecommendations()
    }
}