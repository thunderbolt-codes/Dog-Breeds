package dev.thunderbolt.dogbreeds.data.remote

import dev.thunderbolt.dogbreeds.data.remote.entity.BreedList
import retrofit2.Response
import retrofit2.http.GET

interface DogBreedApi {
    @GET("breeds/list/all")
    suspend fun getBreedList(): Response<BreedList>
}
