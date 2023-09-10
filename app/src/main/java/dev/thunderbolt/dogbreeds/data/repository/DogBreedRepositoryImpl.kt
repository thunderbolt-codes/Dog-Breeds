package dev.thunderbolt.dogbreeds.data.repository

import dev.thunderbolt.dogbreeds.data.ext.parseResponse
import dev.thunderbolt.dogbreeds.data.local.DogBreedDb
import dev.thunderbolt.dogbreeds.data.local.entity.DogBreedEntity
import dev.thunderbolt.dogbreeds.data.local.entity.FavoriteImageEntity
import dev.thunderbolt.dogbreeds.data.remote.DogBreedApi
import dev.thunderbolt.dogbreeds.data.remote.entity.ApiResponse
import dev.thunderbolt.dogbreeds.data.remote.entity.BreedImages
import dev.thunderbolt.dogbreeds.data.remote.entity.BreedList
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DogBreedRepositoryImpl @Inject constructor(
    private val api: DogBreedApi,
    private val db: DogBreedDb,
) : DogBreedRepository {

    override fun getBreedList(): Flow<List<DogBreed>> = flow {
        // FETCH LOCAL DATA & EMIT (IF EXISTS)
        val localData = db.dogBreedDao.getAll()
        if (localData.isNotEmpty()) emit(localData.map { DogBreed(it.name) })

        // MAKE API CALL & INSERT TO DATABASE & EMIT
        when (val response = api.getBreedList().parseResponse()) {
            is ApiResponse.Success<*> -> {
                val remoteData = with(response.data as BreedList) {
                    breedsAndSubBreeds.map { DogBreed(it.key) }
                }
                db.dogBreedDao.insertAll(remoteData.map { DogBreedEntity(name = it.name) })
                emit(remoteData)
            }

            is ApiResponse.Error -> {
                error(response.errMessage)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getBreedImages(breed: String): Flow<List<DogImage>> = flow {
        when (val response = api.getBreedImages(breed).parseResponse()) {
            is ApiResponse.Success<*> -> {
                val remoteData = (response.data as BreedImages).imageUrls
                val breedFavorites = db.favoriteImageDao.getByBreed(breed).map { it.url }
                emit(remoteData.map {
                    DogImage(
                        url = it,
                        isFavorite = breedFavorites.contains(it)
                    )
                })
            }

            is ApiResponse.Error -> {
                error(response.errMessage)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun toggleImageFavorite(breed: String, image: DogImage): Flow<Boolean> = flow {
        if (image.isFavorite) {
            db.favoriteImageDao.deleteByUrl(image.url)
            emit(false)
        } else {
            db.favoriteImageDao.insert(
                FavoriteImageEntity(
                    breed = breed,
                    url = image.url,
                )
            )
            emit(true)
        }
    }.flowOn(Dispatchers.IO)
}
