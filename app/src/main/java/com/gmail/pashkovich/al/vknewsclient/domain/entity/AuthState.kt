package com.gmail.pashkovich.al.vknewsclient.domain.entity

sealed class AuthState {

    object Authorized: AuthState()

    object NotAuthorized: AuthState()

    object Initial: AuthState()
}