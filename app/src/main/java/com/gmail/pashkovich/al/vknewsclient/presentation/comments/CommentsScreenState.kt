package com.gmail.pashkovich.al.vknewsclient.presentation.comments

import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {

    object Initial: CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
        ): CommentsScreenState()
}
