package dev.thunderbolt.dogbreeds.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.thunderbolt.dogbreeds.data.local.dao.DogBreedDao
import dev.thunderbolt.dogbreeds.data.local.dao.FavoriteImageDao
import dev.thunderbolt.dogbreeds.data.local.entity.DogBreedEntity
import dev.thunderbolt.dogbreeds.data.local.entity.FavoriteImageEntity

@Database(
    entities = [DogBreedEntity::class, FavoriteImageEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DogBreedDb : RoomDatabase() {
    abstract val dogBreedDao: DogBreedDao
    abstract val favoriteImageDao: FavoriteImageDao
}
