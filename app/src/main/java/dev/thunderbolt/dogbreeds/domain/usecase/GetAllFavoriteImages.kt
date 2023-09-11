package dev.thunderbolt.dogbreeds.domain.usecase

import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetAllFavoriteImages @Inject constructor(
    private val repository: DogBreedRepository,
) {
    operator fun invoke(): Flow<Response<List<DogImage>>> {
        return repository.getAllFavoriteImages()
            .map {
                Response.Success(it) as Response<List<DogImage>>
            }
            .onStart {
                emit(Response.Loading())
            }
            .catch {
                emit(Response.Error(it.message.orEmpty()))
            }
    }
}
