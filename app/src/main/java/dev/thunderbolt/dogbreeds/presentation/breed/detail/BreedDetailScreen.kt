package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thunderbolt.dogbreeds.domain.entity.DogImage
import dev.thunderbolt.dogbreeds.domain.entity.UIState

@Composable
fun BreedDetailScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = hiltViewModel<BreedDetailViewModel>()
    val breed by viewModel.breed.collectAsStateWithLifecycle()
    val breedImages by viewModel.breedImages.collectAsStateWithLifecycle()

    BreedDetailContent(
        breed = breed,
        breedImages = breedImages,
        navigateBack = navigateBack,
        onFavoriteClicked = viewModel::toggleImageFavorite,
    )
}

@Composable
fun BreedDetailContent(
    breed: String,
    breedImages: UIState<List<DogImage>>,
    navigateBack: () -> Unit = {},
    onFavoriteClicked: (DogImage) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (breedImages is UIState.Error) {
        LaunchedEffect(key1 = snackbarHostState) {
            snackbarHostState.showSnackbar(breedImages.error)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = breed)
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
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
            ) {
                when (breedImages) {
                    is UIState.Success -> {
                        val images = breedImages.data
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(2),
                        ) {
                            items(
                                count = images.size,
                                key = { index -> images[index].url },
                                itemContent = { index ->
                                    val image = images[index]
                                    BreedImageView(
                                        image = image,
                                        onFavoriteClicked = {
                                            onFavoriteClicked(image)
                                        }
                                    )
                                }
                            )
                        }
                    }

                    else -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BreedDetailPreview() {
    BreedDetailContent(
        breed = "Poodle",
        breedImages = UIState.Success(
            listOf(
                DogImage(
                    url = "url",
                    isFavorite = false,
                ),
                DogImage(
                    url = "url",
                    isFavorite = true,
                ),
                DogImage(
                    url = "url",
                    isFavorite = false,
                ),
            )
        ),
    )
}
