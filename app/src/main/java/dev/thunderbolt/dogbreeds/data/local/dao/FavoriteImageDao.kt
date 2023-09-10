package dev.thunderbolt.dogbreeds.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.thunderbolt.dogbreeds.data.local.entity.FavoriteImageEntity

@Dao
interface FavoriteImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteImageEntity)

    @Query("SELECT * FROM favorite_image")
    suspend fun getAll(): List<FavoriteImageEntity>

    @Query("SELECT * FROM favorite_image WHERE breed=:breed")
    suspend fun getByBreed(breed: String): List<FavoriteImageEntity>

    @Query("DELETE FROM favorite_image WHERE url=:url")
    suspend fun deleteByUrl(url: String)

    @Query("DELETE FROM favorite_image")
    suspend fun clearAll()
}
