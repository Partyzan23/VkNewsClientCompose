package com.gmail.pashkovich.al.vknewsclient.di

import android.content.Context
import com.gmail.pashkovich.al.vknewsclient.data.network.ApiFactory
import com.gmail.pashkovich.al.vknewsclient.data.network.ApiService
import com.gmail.pashkovich.al.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.gmail.pashkovich.al.vknewsclient.domain.repository.NewsFeedRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object{

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService{
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideVKStorage(
            context: Context
        ): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}