package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BreedDetailScreen(
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
) {
    val viewModel = hiltViewModel<BreedDetailViewModel>()
    val name by viewModel.name.collectAsStateWithLifecycle()

    BreedDetailContent(
        name = name,
        navigateBack = navigateBack,
    )
}

@Composable
fun BreedDetailContent(
    name: String,
    navigateBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = name)
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
fun BreedDetailPreview() {
    BreedDetailContent(
        name = "Poodle",
    )
}
