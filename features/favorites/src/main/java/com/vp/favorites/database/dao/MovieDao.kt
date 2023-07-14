package com.vp.favorites.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.favorites.database.entities.PosterEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM items_table")
    suspend fun getAllPosters(): List<PosterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPoster(movie: PosterEntity)
}