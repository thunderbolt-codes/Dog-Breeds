package dev.thunderbolt.dogbreeds.domain.usecase

import app.cash.turbine.test
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllFavoriteImagesTest {

    private lateinit var getAllFavoriteImages: GetAllFavoriteImages

    @MockK
    lateinit var repository: DogBreedRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAllFavoriteImages = GetAllFavoriteImages(repository)
    }

    @Test
    fun `getAllFavoriteImages() - success`() = runTest {
        val result = listOf(
            DogImage(
                url = "url1",
                isFavorite = true,
                breed = "Poodle",
            ),
            DogImage(
                url = "url2",
                isFavorite = true,
                breed = "Labrador",
            ),
            DogImage(
                url = "url3",
                isFavorite = true,
                breed = "Pug",
            ),
        )
        every { repository.getAllFavoriteImages() } returns flow {
            emit(result)
        }

        getAllFavoriteImages().test {
            assertEquals(Response.Loading<List<DogImage>>(), awaitItem())
            assertEquals(Response.Success(result), awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getAllFavoriteImages() }
    }

    @Test
    fun `getAllFavoriteImages() - error`() = runTest {
        val exception = Exception("Unexpected error!")
        every { repository.getAllFavoriteImages() } returns flow {
            throw exception
        }

        getAllFavoriteImages().test {
            assertEquals(Response.Loading<List<DogImage>>(), awaitItem())
            assertEquals(
                Response.Error<List<DogImage>>(exception.message.orEmpty()),
                awaitItem()
            )
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getAllFavoriteImages() }
    }
}
