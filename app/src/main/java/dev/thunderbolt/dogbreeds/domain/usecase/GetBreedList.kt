package dev.thunderbolt.dogbreeds.domain.usecase

import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetBreedList @Inject constructor(
    private val repository: DogBreedRepository,
) {
    operator fun invoke(): Flow<Response<List<DogBreed>>> {
        return repository.getBreedList()
            .map {
                Response.Success(it) as Response<List<DogBreed>>
            }
            .onStart {
                emit(Response.Loading())
            }
            .catch {
                emit(Response.Error(it.message.orEmpty()))
            }
    }
}
