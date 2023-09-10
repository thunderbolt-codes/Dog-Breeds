package dev.thunderbolt.dogbreeds.data.remote.entity

import com.google.gson.annotations.SerializedName

data class BreedImages(
    @SerializedName("message")
    val imageUrls: List<String>,
)
