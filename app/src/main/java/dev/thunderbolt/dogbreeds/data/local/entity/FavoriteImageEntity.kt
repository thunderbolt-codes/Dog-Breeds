package dev.thunderbolt.dogbreeds.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite_image")
data class FavoriteImageEntity(
    @PrimaryKey val url: String,
    val breed: String,
)
