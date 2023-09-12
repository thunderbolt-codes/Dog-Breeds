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

class GetBreedImagesTest {

    private lateinit var getBreedImages: GetBreedImages

    @MockK
    lateinit var repository: DogBreedRepository

    private val breed = "Poodle"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getBreedImages = GetBreedImages(repository)
    }

    @Test
    fun `getBreedImages() - success`() = runTest {
        val result = listOf(
            DogImage(
                url = "url1",
                isFavorite = false,
                breed = breed,
            ),
            DogImage(
                url = "url2",
                isFavorite = true,
                breed = breed,
            ),
            DogImage(
                url = "url3",
                isFavorite = false,
                breed = breed,
            ),
        )
        every { repository.getBreedImages(breed) } returns flow {
            emit(result)
        }

        getBreedImages(breed).test {
            assertEquals(Response.Loading<List<DogImage>>(), awaitItem())
            assertEquals(Response.Success(result), awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getBreedImages(breed) }
    }

    @Test
    fun `getBreedImages() - error`() = runTest {
        val exception = Exception("Unexpected error!")
        every { repository.getBreedImages(breed) } returns flow {
            throw exception
        }

        getBreedImages(breed).test {
            assertEquals(Response.Loading<List<DogImage>>(), awaitItem())
            assertEquals(
                Response.Error<List<DogImage>>(exception.message.orEmpty()),
                awaitItem()
            )
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getBreedImages(breed) }
    }
}
