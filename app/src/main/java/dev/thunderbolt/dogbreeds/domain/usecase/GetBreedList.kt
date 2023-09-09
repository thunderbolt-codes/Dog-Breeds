package dev.thunderbolt.dogbreeds.domain.usecase

import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.UIState
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetBreedList @Inject constructor(
    private val repository: DogBreedRepository,
) {
    operator fun invoke(): Flow<UIState<List<DogBreed>>> {
        return repository.getBreedList()
            .map {
                UIState.Success(it) as UIState<List<DogBreed>>
            }
            .onStart {
                emit(UIState.Loading())
            }
            .catch {
                emit(UIState.Error(it.message.orEmpty()))
            }
    }
}
