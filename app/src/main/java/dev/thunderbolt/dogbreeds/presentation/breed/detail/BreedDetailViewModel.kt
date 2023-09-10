package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.UIState
import dev.thunderbolt.dogbreeds.domain.usecase.GetBreedImages
import dev.thunderbolt.dogbreeds.domain.usecase.ToggleImageFavorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBreedImages: GetBreedImages,
    private val toggleImageFavorite: ToggleImageFavorite,
) : ViewModel() {

    val breed: StateFlow<String> = savedStateHandle.getStateFlow("breed", "")

    private val _breedImages = MutableStateFlow<UIState<List<DogImage>>>(UIState.Loading())
    val breedImages: StateFlow<UIState<List<DogImage>>> = _breedImages.asStateFlow()

    init {
        viewModelScope.launch {
            getBreedImages(breed.value).collect {
                _breedImages.value = it
            }
        }
    }

    fun toggleImageFavorite(image: DogImage) {
        viewModelScope.launch {
            toggleImageFavorite(breed.value, image).collect { updatedImage ->
                val images = _breedImages.value.data.orEmpty()
                _breedImages.value = UIState.Success(
                    images.map { if (it.url == image.url) updatedImage else it }
                )
            }
        }
    }
}
