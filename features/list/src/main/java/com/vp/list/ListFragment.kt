package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.net.Uri.Builder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.detail.DetailActivity
import com.vp.list.R.string
import com.vp.list.databinding.FragmentListBinding
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment: Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    companion object {
        private const val CURRENT_QUERY = "current_query"
        const val TAG = "ListFragment"
    }

    lateinit var binding: FragmentListBinding
    private var currentQuery = "Interview"
    private lateinit var listAdapter: ListAdapter
    private lateinit var gridPagingScrollListener: GridPagingScrollListener

    private lateinit var listViewModel: ListViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY) ?: getString(string.interview)
        }
        initSwipeRefreshLayout()
        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(viewLifecycleOwner, Observer {
            it?.let {
                handleResult(listAdapter, it)
            }
        })
        listViewModel.searchMoviesByTitle(currentQuery, 1)
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeContainer.setOnRefreshListener {
            if (listAdapter.itemCount == 0) {
                listViewModel.searchMoviesByTitle(currentQuery, 1)
            } else {
             binding.swipeContainer.isRefreshing = false
            }
        }
    }

    private fun initBottomNavigation(view: View) {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
           if  (it.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        binding.recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            val gridLayoutManager = GridLayoutManager(
                context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
            )
            layoutManager = gridLayoutManager
            gridPagingScrollListener = GridPagingScrollListener(gridLayoutManager)
            gridPagingScrollListener.setLoadMoreItemsListener(this@ListFragment)
            addOnScrollListener(gridPagingScrollListener)
        }
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
                binding.swipeContainer.isRefreshing = false
            }

            IN_PROGRESS -> {
                showProgressBar()
            }

            else -> {
                showError()
            }
        }
        gridPagingScrollListener.markLoading(false)
    }

    private fun showProgressBar() {
        with(binding) {
            viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
        }
    }

    private fun showList() {
        with(binding) {
            viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
        }
    }

    private fun showError() {
        with(binding) {
            viewAnimator.displayedChild = viewAnimator.indexOfChild(errorText)
        }
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener.markLastPage(true)
        }
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(activity, DetailActivity::class.java)
        val uri = Builder()
        uri.appendQueryParameter(DetailActivity.key, imdbID)
        intent.data = uri.build()
        startActivity(intent)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }
}