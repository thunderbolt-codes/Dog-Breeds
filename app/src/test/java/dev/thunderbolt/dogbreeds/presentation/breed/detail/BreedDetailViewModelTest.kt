package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.usecase.GetBreedImages
import dev.thunderbolt.dogbreeds.domain.usecase.ToggleImageFavorite
import dev.thunderbolt.dogbreeds.presentation.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BreedDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BreedDetailViewModel

    @RelaxedMockK
    lateinit var getBreedImages: GetBreedImages

    @MockK
    lateinit var toggleImageFavorite: ToggleImageFavorite

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BreedDetailViewModel(
            SavedStateHandle(mapOf("breed" to "hound")),
            getBreedImages,
            toggleImageFavorite,
        )
    }

    @Test
    fun `toggleFavorite() - image is set as favorite`() = runTest {
        val image = DogImage(
            url = "url1",
            isFavorite = false,
            breed = "hound",
        )
        every { toggleImageFavorite(image) } returns flow {
            delay(50L)
            emit(image.copy(isFavorite = true))
        }

        viewModel.setBreedImages(listOf(image))
        viewModel.toggleFavorite(image)

        viewModel.breedImages.test {
            assertEquals(listOf(image), awaitItem().data)
            assertEquals(listOf(image.copy(isFavorite = true)), awaitItem().data)
            ensureAllEventsConsumed()
        }

        verify { toggleImageFavorite(image) }
    }

    @Test
    fun `toggleFavorite() - image is removed from favorites`() = runTest {
        val image = DogImage(
            url = "url1",
            isFavorite = true,
            breed = "hound",
        )
        every { toggleImageFavorite(image) } returns flow {
            delay(50L)
            emit(image.copy(isFavorite = false))
        }

        viewModel.setBreedImages(listOf(image))
        viewModel.toggleFavorite(image)

        viewModel.breedImages.test {
            assertEquals(listOf(image), awaitItem().data)
            assertEquals(listOf(image.copy(isFavorite = false)), awaitItem().data)
            ensureAllEventsConsumed()
        }

        verify { toggleImageFavorite(image) }
    }
}
