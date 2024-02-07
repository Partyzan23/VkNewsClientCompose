package com.gmail.pashkovich.al.vknewsclient.di

import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.vknewsclient.presentation.comments.CommentsViewModel
import com.gmail.pashkovich.al.vknewsclient.presentation.main.MainViewModel
import com.gmail.pashkovich.al.vknewsclient.presentation.news.NewsFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    @Binds
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}