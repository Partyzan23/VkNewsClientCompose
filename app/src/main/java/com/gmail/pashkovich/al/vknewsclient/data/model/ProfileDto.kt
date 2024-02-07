package com.gmail.pashkovich.al.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class ProfileDto (
    @SerializedName("id") val id: Long,
    @SerializedName("photo_100") val avatarUrl: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)