package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel

    private lateinit var poster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails(getMovieId())
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.poster().observe(this, Observer {
            poster = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter(key) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.star) {
            if (this::poster.isInitialized) {
                detailViewModel.saveMovie(poster)
            }
        }
        return false
    }

    companion object {
        const val key = "imdbID"
    }
}
