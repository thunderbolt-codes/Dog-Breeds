package dev.thunderbolt.dogbreeds.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.thunderbolt.dogbreeds.data.local.dao.DogBreedDao
import dev.thunderbolt.dogbreeds.data.local.entity.DogBreedEntity

@Database(
    entities = [DogBreedEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DogBreedDb : RoomDatabase() {
    abstract val dogBreedDao: DogBreedDao
}
