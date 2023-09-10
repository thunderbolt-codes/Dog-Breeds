package dev.thunderbolt.dogbreeds.data.remote

import dev.thunderbolt.dogbreeds.data.remote.entity.BreedImages
import dev.thunderbolt.dogbreeds.data.remote.entity.BreedList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogBreedApi {
    @GET("breeds/list/all")
    suspend fun getBreedList(): Response<BreedList>

    @GET("breed/{breed}/images")
    suspend fun getBreedImages(@Path("breed") breed: String): Response<BreedImages>
}
