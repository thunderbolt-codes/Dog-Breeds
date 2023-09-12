package dev.thunderbolt.dogbreeds.presentation.breed.favorites

import app.cash.turbine.test
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.usecase.GetAllFavoriteImages
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

class BreedFavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BreedFavoritesViewModel

    @RelaxedMockK
    lateinit var getAllFavoriteImages: GetAllFavoriteImages

    @MockK
    lateinit var toggleImageFavorite: ToggleImageFavorite

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BreedFavoritesViewModel(
            getAllFavoriteImages,
            toggleImageFavorite,
        )
    }

    @Test
    fun `toggleFavorite() - image is removed from favorites & breed has more fav images`() =
        runTest {
            val breed = "hound"
            val image = DogImage(
                url = "url1",
                isFavorite = true,
                breed = breed,
            )
            every { toggleImageFavorite(image) } returns flow {
                delay(50L)
                emit(image.copy(isFavorite = false))
            }

            val favoriteImages = listOf(image, image.copy(url = "url2"))

            viewModel.setFavoriteImages(favoriteImages)
            viewModel.setSelectedBreeds(listOf(breed))
            viewModel.toggleFavorite(image)

            viewModel.favoriteImages.test {
                assertEquals(favoriteImages, awaitItem().data)
                assertEquals(favoriteImages.takeLast(1), awaitItem().data)
                ensureAllEventsConsumed()
            }
            viewModel.selectedBreeds.test {
                assertEquals(listOf(breed), awaitItem())
                ensureAllEventsConsumed()
            }

            verify { toggleImageFavorite(image) }
        }

    @Test
    fun `toggleFavorite() - image is removed from favorites & breed has no more fav images`() =
        runTest {
            val breed = "hound"
            val image = DogImage(
                url = "url1",
                isFavorite = true,
                breed = breed,
            )
            every { toggleImageFavorite(image) } returns flow {
                delay(50L)
                emit(image.copy(isFavorite = false))
            }

            val favoriteImages = listOf(image)

            viewModel.setFavoriteImages(favoriteImages)
            viewModel.setSelectedBreeds(listOf(breed))
            viewModel.toggleFavorite(image)

            viewModel.favoriteImages.test {
                assertEquals(favoriteImages, awaitItem().data)
                assertEquals(emptyList<DogImage>(), awaitItem().data)
                ensureAllEventsConsumed()
            }
            viewModel.selectedBreeds.test {
                assertEquals(emptyList<String>(), awaitItem())
                ensureAllEventsConsumed()
            }

            verify { toggleImageFavorite(image) }
        }

    @Test
    fun `toggleBreedSelection() - add to list`() = runTest {
        val breed = "hound"

        viewModel.toggleBreedSelection(breed)

        viewModel.selectedBreeds.test {
            assertEquals(listOf(breed), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `toggleBreedSelection() - remove from list`() = runTest {
        val breed = "hound"

        viewModel.setSelectedBreeds(listOf(breed))
        viewModel.toggleBreedSelection(breed)

        viewModel.selectedBreeds.test {
            assertEquals(emptyList<String>(), awaitItem())
            ensureAllEventsConsumed()
        }
    }
}
