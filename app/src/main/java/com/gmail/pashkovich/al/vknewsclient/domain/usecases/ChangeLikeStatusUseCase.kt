package com.gmail.pashkovich.al.vknewsclient.domain.usecases

import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.entity.PostComment
import com.gmail.pashkovich.al.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(feedPost: FeedPost){
        repository.changeLikeStatus(feedPost)
    }
}