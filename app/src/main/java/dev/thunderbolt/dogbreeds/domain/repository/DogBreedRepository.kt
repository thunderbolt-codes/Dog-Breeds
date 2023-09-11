package dev.thunderbolt.dogbreeds.domain.repository

import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import kotlinx.coroutines.flow.Flow

interface DogBreedRepository {
    fun getBreedList(): Flow<List<DogBreed>>
    fun getBreedImages(breed: String): Flow<List<DogImage>>
    fun toggleImageFavorite(image: DogImage): Flow<Boolean>
    fun getAllFavoriteImages(): Flow<List<DogImage>>
}
