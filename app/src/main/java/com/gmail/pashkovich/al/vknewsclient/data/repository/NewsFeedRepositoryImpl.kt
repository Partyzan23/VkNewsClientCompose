package com.gmail.pashkovich.al.vknewsclient.data.repository

import android.app.Application
import android.util.Log
import com.gmail.pashkovich.al.vknewsclient.data.mapper.NewsFeedMapper
import com.gmail.pashkovich.al.vknewsclient.data.network.ApiFactory
import com.gmail.pashkovich.al.vknewsclient.data.network.ApiService
import com.gmail.pashkovich.al.vknewsclient.domain.entity.*
import com.gmail.pashkovich.al.vknewsclient.domain.repository.NewsFeedRepository
import com.gmail.pashkovich.al.vknewsclient.extensions.mergeWith
import com.gmail.pashkovich.al.vknewsclient.presentation.news.getItemByType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val mapper: NewsFeedMapper,
    private val apiService: ApiService
) : NewsFeedRepository {
    private val token get() = VKAccessToken.restore(storage)


    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost> get() = _feedPosts.toList()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var nextFrom: String? = null

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    private val nextDataNeedEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshListFlow = MutableSharedFlow<List<FeedPost>>()
    private val loadedListFlow = flow {
        nextDataNeedEvents.emit(Unit)
        nextDataNeedEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val response = if (startFrom == null) {
                apiService.loadRecommendations(getAccessToken())
            } else {
                apiService.loadRecommendations(getAccessToken(), startFrom)
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapResponseToPosts(response)
            _feedPosts.addAll(posts)
            Log.d("TEST_TEST", "owner_id = ${posts[0].communityId}, id = ${posts[0].id}")
            emit(feedPosts)
        }
    }.retry() {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.catch { }


    private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts

        )

    override suspend fun loadNextData() {
        nextDataNeedEvents.emit(Unit)
    }

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return authStateFlow
    }

    override fun getRecommendations(): StateFlow<List<FeedPost>> {
        return recommendations
    }

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val count: Int =
            if (feedPost.statistics.getItemByType(StatisticType.COMMENTS).count >= 100) {
                100
            } else {
                feedPost.statistics.getItemByType(StatisticType.COMMENTS).count
            }
        val comments = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            count = count
        )
        emit(mapper.mapResponseToComments(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is not valid")
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(
            isLiked = !feedPost.isLiked,
            statistics = newStatistics
        )
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshListFlow.emit(feedPosts)
    }

    override suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        refreshListFlow.emit(feedPosts)
    }

    companion object {
        const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}