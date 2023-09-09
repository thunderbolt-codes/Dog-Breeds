package dev.thunderbolt.dogbreeds.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("dog_breed")
data class DogBreedEntity(
    @PrimaryKey val name: String,
)
