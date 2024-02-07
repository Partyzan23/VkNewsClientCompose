package com.gmail.pashkovich.al.vknewsclient.di

import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent (
    modules = [
        CommentsViewModelModule::class
    ]
        )
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance feedPost: FeedPost
        ): CommentsScreenComponent
    }
}