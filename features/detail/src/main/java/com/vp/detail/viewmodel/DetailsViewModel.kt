package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.detail.viewmodel.DetailsViewModel.LoadingState.ERROR
import com.vp.detail.viewmodel.DetailsViewModel.LoadingState.LOADED
import com.vp.favorites.database.dao.MovieDao
import com.vp.favorites.database.entities.PosterEntity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val detailService: DetailService/*, private val moviesDao: MovieDao*/) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val poster: MutableLiveData<String> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun poster(): LiveData<String> = poster

    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object: Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                details.postValue(response.body())

                response.body()?.title?.let {
                    title.postValue(it)
                }
                response.body()?.poster?.let {
                    poster.postValue(it)
                }

                loadingState.value = LOADED
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                details.postValue(null)
                loadingState.value = ERROR
            }
        })
    }

    fun saveMovie(poster: String) {
        viewModelScope.launch {
            //moviesDao.addPoster(PosterEntity(poster))
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}