package com.vp.favorites.di

import android.content.Context
import androidx.room.Room
import com.vp.favorites.database.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    companion object {
        private const val MOVIES_DATABASE_NAME = "movies_database"
    }

    @Singleton
    @Provides
    fun providesDatabase(context: Context): MoviesDatabase = Room.databaseBuilder(context, MoviesDatabase::class.java, MOVIES_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesMoviesDao(db: MoviesDatabase) = db.getMoviesDao()
}