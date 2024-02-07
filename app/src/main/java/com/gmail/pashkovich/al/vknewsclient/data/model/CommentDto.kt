package com.gmail.pashkovich.al.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("id") val commentId: Long,
    @SerializedName("from_id") val authorId: Long,
    @SerializedName("text") val textComment: String,
    @SerializedName("date") val dateComment: Long
)
