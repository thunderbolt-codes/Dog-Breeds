package dev.thunderbolt.dogbreeds.domain.usecase

import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToggleImageFavorite @Inject constructor(
    private val repository: DogBreedRepository,
) {
    operator fun invoke(breed: String, image: DogImage): Flow<DogImage> {
        return repository.toggleImageFavorite(breed, image)
            .map {
                image.copy(isFavorite = it)
            }
            .catch {
                emit(image)
            }
    }
}
