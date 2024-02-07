package com.gmail.pashkovich.al.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val feedPost: FeedPost
): ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}