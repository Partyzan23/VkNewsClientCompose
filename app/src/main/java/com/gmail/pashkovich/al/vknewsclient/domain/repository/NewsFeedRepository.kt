package com.gmail.pashkovich.al.vknewsclient.domain.repository

import com.gmail.pashkovich.al.vknewsclient.domain.entity.AuthState
import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository{

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getRecommendations():StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>>

    suspend fun loadNextData()

    suspend fun checkAuthState()

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun deletePost(feedPost: FeedPost)
}