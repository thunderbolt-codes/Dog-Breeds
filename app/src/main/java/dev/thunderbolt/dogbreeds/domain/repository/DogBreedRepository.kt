package dev.thunderbolt.dogbreeds.domain.repository

import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import kotlinx.coroutines.flow.Flow

interface DogBreedRepository {
    fun getBreedList(): Flow<List<DogBreed>>
}
