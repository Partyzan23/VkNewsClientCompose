package com.gmail.pashkovich.al.vknewsclient.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class PostComment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String
)
