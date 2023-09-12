package dev.thunderbolt.dogbreeds.data.repository

import app.cash.turbine.test
import com.google.gson.Gson
import dev.thunderbolt.dogbreeds.data.local.DogBreedDb
import dev.thunderbolt.dogbreeds.data.local.dao.DogBreedDao
import dev.thunderbolt.dogbreeds.data.local.dao.FavoriteImageDao
import dev.thunderbolt.dogbreeds.data.local.entity.DogBreedEntity
import dev.thunderbolt.dogbreeds.data.local.entity.FavoriteImageEntity
import dev.thunderbolt.dogbreeds.data.remote.DogBreedApi
import dev.thunderbolt.dogbreeds.data.remote.entity.ApiResponse
import dev.thunderbolt.dogbreeds.data.remote.entity.BreedImages
import dev.thunderbolt.dogbreeds.data.remote.entity.BreedList
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DogBreedRepositoryImplTest {

    private lateinit var repository: DogBreedRepositoryImpl

    @MockK
    lateinit var api: DogBreedApi

    @MockK
    lateinit var db: DogBreedDb

    @RelaxedMockK
    lateinit var dogBreedDao: DogBreedDao

    @RelaxedMockK
    lateinit var favoriteImageDao: FavoriteImageDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = DogBreedRepositoryImpl(api, db)

        every { db.dogBreedDao } returns dogBreedDao
        every { db.favoriteImageDao } returns favoriteImageDao
    }

    @Test
    fun `getBreedList() - api success & local data found`() = runTest {
        val apiResponse = Response.success(
            BreedList(
                mapOf(
                    "Poodle" to listOf(),
                    "Labrador" to listOf(),
                    "Pug" to listOf()
                )
            )
        )
        coEvery { api.getBreedList() } returns apiResponse

        val dbResponse = listOf(
            DogBreedEntity("Poodle"),
            DogBreedEntity("Labrador"),
        )
        coEvery { dogBreedDao.getAll() } returns dbResponse

        val localData = dbResponse.map { DogBreed(it.name) }
        val remoteData = apiResponse.body()!!.breedsAndSubBreeds.map { DogBreed(it.key) }

        repository.getBreedList().test {
            assertEquals(localData, awaitItem())
            assertEquals(remoteData, awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { api.getBreedList() }
        coVerify { dogBreedDao.getAll() }
        coVerify { dogBreedDao.insertAll(any()) }
    }

    @Test
    fun `getBreedList() - api success & local data not found`() = runTest {
        val apiResponse = Response.success(
            BreedList(
                mapOf(
                    "Poodle" to listOf(),
                    "Labrador" to listOf(),
                    "Pug" to listOf()
                )
            )
        )
        coEvery { api.getBreedList() } returns apiResponse

        coEvery { dogBreedDao.getAll() } returns listOf()

        val remoteData = apiResponse.body()!!.breedsAndSubBreeds.map { DogBreed(it.key) }

        repository.getBreedList().test {
            assertEquals(remoteData, awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { api.getBreedList() }
        coVerify { dogBreedDao.getAll() }
        coVerify { dogBreedDao.insertAll(any()) }
    }

    @Test
    fun `getBreedList() - api error`() = runTest {
        val error = ApiResponse.Error("Not found", 404)
        val apiResponse = Response.error<BreedList>(
            error.code,
            Gson().toJson(error).toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { api.getBreedList() } returns apiResponse

        repository.getBreedList().test {
            assertEquals(error.errMessage, awaitError().message)
            ensureAllEventsConsumed()
        }

        coVerify { api.getBreedList() }
        coVerify { dogBreedDao.getAll() }
        coVerify(exactly = 0) { dogBreedDao.insertAll(any()) }
    }

    @Test
    fun `getBreedImages() - api success`() = runTest {
        val breed = "Poodle"
        val apiResponse = Response.success(
            BreedImages(
                listOf("url1", "url2", "url3")
            )
        )
        coEvery { api.getBreedImages(breed) } returns apiResponse

        val dbResponse = listOf(
            FavoriteImageEntity("url1", "Poodle"),
            FavoriteImageEntity("url2", "Poodle"),
        )
        coEvery { favoriteImageDao.getByBreed(breed) } returns dbResponse

        val remoteData = apiResponse.body()!!.imageUrls
        val breedFavorites = dbResponse.map { it.url }
        val expected = remoteData.map {
            DogImage(
                url = it,
                isFavorite = breedFavorites.contains(it),
                breed = breed,
            )
        }

        repository.getBreedImages(breed).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { api.getBreedImages(breed) }
        coVerify { favoriteImageDao.getByBreed(breed) }
    }


    @Test
    fun `getBreedImages() - api error`() = runTest {
        val breed = "Poodle"
        val error = ApiResponse.Error("Not found", 404)
        val apiResponse = Response.error<BreedImages>(
            error.code,
            Gson().toJson(error).toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { api.getBreedImages(breed) } returns apiResponse

        repository.getBreedImages(breed).test {
            assertEquals(error.errMessage, awaitError().message)
            ensureAllEventsConsumed()
        }

        coVerify { api.getBreedImages(breed) }
        coVerify(exactly = 0) { favoriteImageDao.getByBreed(breed) }
    }

    @Test
    fun `toggleImageFavorite() - isFavorite = true`() = runTest {
        val image = DogImage(
            url = "url1",
            isFavorite = true,
            breed = "Poodle",
        )
        coEvery { favoriteImageDao.deleteByUrl(image.url) } returns Unit

        repository.toggleImageFavorite(image).test {
            assertFalse(awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { favoriteImageDao.deleteByUrl(image.url) }
    }

    @Test
    fun `toggleImageFavorite() - isFavorite = false`() = runTest {
        val image = DogImage(
            url = "url1",
            isFavorite = false,
            breed = "Poodle",
        )
        val entity = FavoriteImageEntity(
            breed = image.breed,
            url = image.url,
        )
        coEvery { favoriteImageDao.insert(entity) } returns Unit

        repository.toggleImageFavorite(image).test {
            assertTrue(awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { favoriteImageDao.insert(entity) }
    }

    @Test
    fun `getAllFavoriteImages() - success`() = runTest {
        val dbResponse = listOf(
            FavoriteImageEntity("url1", "Poodle"),
            FavoriteImageEntity("url2", "Labrador"),
        )
        coEvery { favoriteImageDao.getAll() } returns dbResponse

        val expected = dbResponse.map {
            DogImage(
                url = it.url,
                isFavorite = true,
                breed = it.breed,
            )
        }

        repository.getAllFavoriteImages().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        coVerify { favoriteImageDao.getAll() }
    }
}
