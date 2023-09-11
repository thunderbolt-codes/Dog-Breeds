package dev.thunderbolt.dogbreeds.presentation.breed.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed

@Composable
fun BreedItemView(
    dogBreed: DogBreed,
    onClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clickable { onClicked() }
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = dogBreed.name,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BreedItemPreview() {
    BreedItemView(
        dogBreed = DogBreed("Poodle"),
    )
}
