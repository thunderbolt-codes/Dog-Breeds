package dev.thunderbolt.dogbreeds.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.thunderbolt.dogbreeds.data.local.entity.DogBreedEntity

@Dao
interface DogBreedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DogBreedEntity>)

    @Query("SELECT * FROM dog_breed")
    suspend fun getAll(): List<DogBreedEntity>

    @Query("SELECT * FROM dog_breed WHERE name=:name")
    suspend fun getByName(name: String): DogBreedEntity?

    @Query("DELETE FROM dog_breed")
    suspend fun clearAll()
}
