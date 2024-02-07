package com.gmail.pashkovich.al.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gmail.pashkovich.al.vknewsclient.domain.entity.AuthState
import com.gmail.pashkovich.al.vknewsclient.presentation.NewsFeedApplication
import com.gmail.pashkovich.al.vknewsclient.presentation.ViewModelFactory
import com.gmail.pashkovich.al.vknewsclient.presentation.getApplicationComponent
import com.gmail.pashkovich.al.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(initial = AuthState.Initial)
            val launcherAuth = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract(),
            ) {
                viewModel.performAuthResult()
            }
            VkNewsClientTheme {
                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }
                    is AuthState.NotAuthorized -> {
                        LoginScreen {
                            launcherAuth.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }
}