package dev.thunderbolt.dogbreeds.data.remote.entity

import com.google.gson.annotations.SerializedName

data class BreedList(
    @SerializedName("message")
    val breedsAndSubBreeds: Map<String, List<String>>,
)
