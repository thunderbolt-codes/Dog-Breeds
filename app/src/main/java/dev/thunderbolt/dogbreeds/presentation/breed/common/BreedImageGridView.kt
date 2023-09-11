package dev.thunderbolt.dogbreeds.presentation.breed.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.thunderbolt.dogbreeds.R
import dev.thunderbolt.dogbreeds.domain.entity.DogImage

@Composable
fun BreedImageGridView(
    images: List<DogImage>,
    onFavoriteClicked: (DogImage) -> Unit = {},
) {
    if (images.isEmpty()) {
        Text(
            text = stringResource(R.string.common_no_image_message),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        )
        return
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(
            count = images.size,
            key = { index -> images[index].url },
        ) { index ->
            val image = images[index]
            BreedImageView(
                image = image,
                onFavoriteClicked = {
                    onFavoriteClicked(image)
                }
            )
        }
    }
}
