package com.vp.list

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import com.vp.list.R.id
import com.vp.list.databinding.ActivityMovieListBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasAndroidInjector {

    companion object {
        const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        const val CURRENT_QUERY = "current_query"
    }

    private lateinit var searchView: SearchView
    private var searchViewExpanded = true
    private lateinit var binding: ActivityMovieListBinding
    private var currentQuery = ""

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentQuery = it.getString(CURRENT_QUERY) ?: ""
            searchViewExpanded = it.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(id.fragmentContainer, ListFragment(), ListFragment.TAG)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu?.findItem(R.id.search)

        searchView = menuItem?.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.isIconified = searchViewExpanded

        if (currentQuery != "") {
            searchView.setQuery(currentQuery, false)
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
                listFragment?.submitSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                currentQuery = newText
                return false
            }
        })

        return true
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingActivityInjector
}