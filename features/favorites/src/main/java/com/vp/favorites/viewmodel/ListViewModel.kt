package com.vp.favorites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.favorites.database.dao.MovieDao
import com.vp.favorites.database.entities.PosterEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(val movieDao: MovieDao): ViewModel() {

    private val liveData = MutableLiveData<List<PosterEntity>>()

    fun observeMovies() = liveData

    fun getAllFavoriteMovies() {
        viewModelScope.launch {
            val posters = movieDao.getAllPosters()
            liveData.value = posters
        }

    }
}