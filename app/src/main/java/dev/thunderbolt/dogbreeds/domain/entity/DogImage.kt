package dev.thunderbolt.dogbreeds.domain.entity

data class DogImage(
    val url: String,
    val isFavorite: Boolean,
    val breed: String,
)
