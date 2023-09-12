package dev.thunderbolt.dogbreeds.presentation.breed.favorites

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.usecase.GetAllFavoriteImages
import dev.thunderbolt.dogbreeds.domain.usecase.ToggleImageFavorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedFavoritesViewModel @Inject constructor(
    private val getAllFavoriteImages: GetAllFavoriteImages,
    private val toggleImageFavorite: ToggleImageFavorite,
) : ViewModel() {

    private val _favoriteImages = MutableStateFlow<Response<List<DogImage>>>(Response.Loading())
    val favoriteImages: StateFlow<Response<List<DogImage>>> = _favoriteImages.asStateFlow()

    private val _selectedBreeds = MutableStateFlow<List<String>>(listOf())
    val selectedBreeds: StateFlow<List<String>> = _selectedBreeds.asStateFlow()

    init {
        viewModelScope.launch {
            getAllFavoriteImages().collect {
                _favoriteImages.value = it
            }
        }
    }

    fun toggleFavorite(image: DogImage) {
        viewModelScope.launch {
            toggleImageFavorite(image).collect { updatedImage ->
                // YOU CAN ONLY REMOVE HERE
                if (updatedImage.isFavorite.not()) {
                    val images = _favoriteImages.value.data.orEmpty()
                    _favoriteImages.value = Response.Success(
                        images.filterNot { it.url == image.url }
                    )

                    // CHECK IF NO IMAGE LEFT FOR THIS BREED
                    // THEN REMOVE IT FROM SELECTED BREEDS
                    val noImageLeftForThisBreed =
                        _favoriteImages.value.data.orEmpty().none { it.breed == image.breed }
                    if (noImageLeftForThisBreed)
                        _selectedBreeds.value = _selectedBreeds.value.minus(image.breed)
                }
            }
        }
    }

    fun toggleBreedSelection(breed: String) {
        _selectedBreeds.value = _selectedBreeds.value.let {
            if (it.contains(breed)) it.minus(breed) else it.plus(breed)
        }
    }

    @VisibleForTesting
    fun setFavoriteImages(images: List<DogImage>) {
        _favoriteImages.value = Response.Success(images)
    }

    @VisibleForTesting
    fun setSelectedBreeds(breeds: List<String>) {
        _selectedBreeds.value = breeds
    }
}
