package com.gmail.pashkovich.al.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.DeletePostUseCase
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.GetRecommendationsUseCase
import com.gmail.pashkovich.al.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.gmail.pashkovich.al.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase
): ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("TEST_TEST", "Exception caught by exception handler")
    }

    private val recommendationsFlow = getRecommendationsUseCase()

    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            loadNextDataUseCase()
        }
    }


    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun deleteFeedPost(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(feedPost)
        }
    }
}