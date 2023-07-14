package com.vp.favorites.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.favorites.database.dao.MovieDao
import com.vp.favorites.database.entities.PosterEntity

@Database(entities = [PosterEntity::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun getMoviesDao(): MovieDao
}