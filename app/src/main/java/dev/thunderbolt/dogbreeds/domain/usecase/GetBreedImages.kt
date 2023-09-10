package dev.thunderbolt.dogbreeds.domain.usecase

import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.UIState
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetBreedImages @Inject constructor(
    private val repository: DogBreedRepository,
) {
    operator fun invoke(breed: String): Flow<UIState<List<DogImage>>> {
        return repository.getBreedImages(breed)
            .map {
                UIState.Success(it) as UIState<List<DogImage>>
            }
            .onStart {
                emit(UIState.Loading())
            }
            .catch {
                emit(UIState.Error(it.message.orEmpty()))
            }
    }
}
