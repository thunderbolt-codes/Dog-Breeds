package dev.thunderbolt.dogbreeds.presentation.breed.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thunderbolt.dogbreeds.R
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.UIState

@Composable
fun BreedListScreen(
    navigateToDetail: (String) -> Unit,
    navigateToFavorites: () -> Unit,
) {
    val viewModel = hiltViewModel<BreedListViewModel>()
    val breedList by viewModel.breedList.collectAsStateWithLifecycle()

    BreedListContent(
        breedList = breedList,
        navigateToDetail = navigateToDetail,
        navigateToFavorites = navigateToFavorites,
    )
}


@Composable
fun BreedListContent(
    breedList: UIState<List<DogBreed>>,
    navigateToDetail: (String) -> Unit = {},
    navigateToFavorites: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (breedList is UIState.Error) {
        LaunchedEffect(key1 = snackbarHostState) {
            snackbarHostState.showSnackbar(breedList.error)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { navigateToFavorites() }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.favorites),
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
                when (breedList) {
                    is UIState.Success -> {
                        val breeds = breedList.data
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                count = breeds.size,
                                key = { index -> breeds[index].name },
                                itemContent = { index ->
                                    val dogBreed = breeds[index]
                                    BreedItemView(
                                        dogBreed = dogBreed,
                                        onClicked = { navigateToDetail(dogBreed.name) },
                                    )
                                    Divider()
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
fun BreedListPreview() {
    BreedListContent(
        breedList = UIState.Success(
            listOf(
                DogBreed("Poodle"),
                DogBreed("Labrador"),
                DogBreed("Pug"),
                DogBreed("Husky"),
            )
        )
    )
}
