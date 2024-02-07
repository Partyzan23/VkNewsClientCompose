package com.gmail.pashkovich.al.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class LikesCountDto(
    @SerializedName("likes") val count: Int
)