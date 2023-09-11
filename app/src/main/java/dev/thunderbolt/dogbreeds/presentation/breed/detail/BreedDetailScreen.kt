package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.presentation.breed.common.BreedImageGridView

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
        onFavoriteClicked = viewModel::toggleFavorite,
    )
}

@Composable
fun BreedDetailContent(
    breed: String,
    breedImages: Response<List<DogImage>>,
    navigateBack: () -> Unit = {},
    onFavoriteClicked: (DogImage) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (breedImages is Response.Error) {
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
                    is Response.Success -> BreedImageGridView(
                        images = breedImages.data,
                        onFavoriteClicked = onFavoriteClicked,
                    )

                    else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
        breedImages = Response.Success(
            listOf(
                DogImage(
                    url = "url1",
                    isFavorite = false,
                    breed = "Poodle",
                ),
                DogImage(
                    url = "url2",
                    isFavorite = true,
                    breed = "Poodle",
                ),
                DogImage(
                    url = "url3",
                    isFavorite = false,
                    breed = "Poodle",
                ),
            )
        ),
    )
}
