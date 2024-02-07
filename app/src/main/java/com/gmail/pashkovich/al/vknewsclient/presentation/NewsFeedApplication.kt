package com.gmail.pashkovich.al.vknewsclient.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.gmail.pashkovich.al.vknewsclient.di.ApplicationComponent
import com.gmail.pashkovich.al.vknewsclient.di.DaggerApplicationComponent
import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost

class NewsFeedApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this
        )
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("RECOMPOSITION_TAG", "getApplicationComponent")
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}