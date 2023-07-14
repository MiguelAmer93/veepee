package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri.Builder
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.viewmodel.ListViewModel

class FavoriteActivity: AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var listViewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initList()
        listViewModel.getAllFavoriteMovies()
        listViewModel.observeMovies().observe(this, Observer {
            listAdapter.setItems(it)
        })
    }


    fun initList() {
        listAdapter = ListAdapter()
        binding.recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            val gridLayoutManager = GridLayoutManager(
                context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
            )
            layoutManager = gridLayoutManager
        }
    }

}