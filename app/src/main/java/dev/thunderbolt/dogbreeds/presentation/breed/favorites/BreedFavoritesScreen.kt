package dev.thunderbolt.dogbreeds.presentation.breed.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thunderbolt.dogbreeds.R
import dev.thunderbolt.dogbreeds.presentation.breed.detail.BreedDetailContent
import dev.thunderbolt.dogbreeds.presentation.breed.detail.BreedDetailViewModel

@Composable
fun BreedFavoritesScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = hiltViewModel<BreedFavoritesViewModel>()

    BreedFavoritesContent(
        navigateBack = navigateBack,
    )
}

@Composable
fun BreedFavoritesContent(
    navigateBack: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
            ) {


            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BreedFavoritesPreview() {
    BreedFavoritesContent(

    )
}
