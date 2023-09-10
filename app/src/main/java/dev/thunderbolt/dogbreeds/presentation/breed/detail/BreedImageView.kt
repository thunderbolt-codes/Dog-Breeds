package dev.thunderbolt.dogbreeds.presentation.breed.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.thunderbolt.dogbreeds.R
import dev.thunderbolt.dogbreeds.domain.entity.DogImage

@Composable
fun BreedImageView(
    image: DogImage,
    onFavoriteClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.background)
            .aspectRatio(1f)
            .fillMaxWidth(),
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.url)
                .placeholder(R.drawable.ic_placeholder_dog)
                .error(R.drawable.ic_placeholder_dog)
                .crossfade(true)
                .build(),
            contentDescription = "Dog Image",
            contentScale = ContentScale.Crop,
        )
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), CircleShape)
                .align(Alignment.TopEnd),
            onClick = { onFavoriteClicked() }) {
            Icon(
                imageVector = if (image.isFavorite)
                    Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "Favorite",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreedImagePreview() {
    BreedImageView(
        image = DogImage(
            url = "url",
            isFavorite = false,
        ),
    )
}
