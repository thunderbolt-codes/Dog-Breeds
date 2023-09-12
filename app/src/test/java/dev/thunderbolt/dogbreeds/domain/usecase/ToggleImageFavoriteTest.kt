package dev.thunderbolt.dogbreeds.domain.usecase

import app.cash.turbine.test
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
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

class ToggleImageFavoriteTest {

    private lateinit var toggleImageFavorite: ToggleImageFavorite

    @MockK
    lateinit var repository: DogBreedRepository

    private val image = DogImage(
        url = "url",
        isFavorite = false,
        breed = "Poodle",
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        toggleImageFavorite = ToggleImageFavorite(repository)
    }

    @Test
    fun `toggleImageFavorite() - success`() = runTest {
        val result = true
        every { repository.toggleImageFavorite(image) } returns flow {
            emit(result)
        }

        toggleImageFavorite(image).test {
            assertEquals(image.copy(isFavorite = result), awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.toggleImageFavorite(image) }
    }

    @Test
    fun `toggleImageFavorite() - error`() = runTest {
        val exception = Exception("Unexpected error!")
        every { repository.toggleImageFavorite(image) } returns flow {
            throw exception
        }

        toggleImageFavorite(image).test {
            assertEquals(image, awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.toggleImageFavorite(image) }
    }
}
