package dev.thunderbolt.dogbreeds.presentation.breed.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thunderbolt.dogbreeds.R
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.presentation.breed.common.BreedImageGridView

@Composable
fun BreedFavoritesScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = hiltViewModel<BreedFavoritesViewModel>()
    val favoriteImages by viewModel.favoriteImages.collectAsStateWithLifecycle()
    val selectedBreeds by viewModel.selectedBreeds.collectAsStateWithLifecycle()

    val favoriteBreeds by remember {
        derivedStateOf {
            favoriteImages.data.orEmpty().map { it.breed }.distinct().map {
                DogBreed(
                    name = it,
                    isSelected = selectedBreeds.contains(it),
                )
            }
        }
    }
    val filteredImages by remember {
        derivedStateOf {
            if (favoriteImages.isSuccessful().not() || selectedBreeds.isEmpty())
                return@derivedStateOf favoriteImages

            return@derivedStateOf Response.Success(
                favoriteImages.data.orEmpty().filter { selectedBreeds.contains(it.breed) }
            )
        }
    }

    BreedFavoritesContent(
        favoriteBreeds = favoriteBreeds,
        filteredImages = filteredImages,
        navigateBack = navigateBack,
        onFavoriteClicked = viewModel::toggleFavorite,
        onBreedClicked = viewModel::toggleBreedSelection,
    )
}

@Composable
fun BreedFavoritesContent(
    favoriteBreeds: List<DogBreed>,
    filteredImages: Response<List<DogImage>>,
    navigateBack: () -> Unit = {},
    onFavoriteClicked: (DogImage) -> Unit = {},
    onBreedClicked: (String) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (filteredImages is Response.Error) {
        LaunchedEffect(key1 = snackbarHostState) {
            snackbarHostState.showSnackbar(filteredImages.error)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.favorites))
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                BreedChipsListView(
                    favoriteBreeds = favoriteBreeds,
                    onBreedClicked = onBreedClicked,
                )
                when (filteredImages) {
                    is Response.Success -> BreedImageGridView(
                        images = filteredImages.data,
                        onFavoriteClicked = onFavoriteClicked,
                    )

                    else -> CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    )
}

@Composable
fun BreedChipsListView(
    favoriteBreeds: List<DogBreed>,
    onBreedClicked: (String) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
    ) {
        items(
            count = favoriteBreeds.size,
            key = { index -> favoriteBreeds[index].name },
        ) { index ->
            val breed = favoriteBreeds[index]
            FilterChip(
                selected = breed.isSelected,
                onClick = { onBreedClicked(breed.name) },
                label = {
                    Text(text = breed.name)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreedFavoritesPreview() {
    BreedFavoritesContent(
        favoriteBreeds = listOf(
            DogBreed("Poodle"),
            DogBreed("Labrador"),
            DogBreed("Pug"),
        ),
        filteredImages = Response.Loading(
            listOf(
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
        ),
    )
}
