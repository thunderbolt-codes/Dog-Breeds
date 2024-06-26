package dev.thunderbolt.dogbreeds.presentation.breed.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.usecase.GetBreedList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val getBreedList: GetBreedList,
) : ViewModel() {

    val breedList: StateFlow<Response<List<DogBreed>>> = getBreedList()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            Response.Loading(),
        )
}
