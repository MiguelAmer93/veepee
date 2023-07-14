package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState.ERROR
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED

data class SearchResult(var items: List<ListItem>, var totalResult: Int, var listState: ListState) {

    companion object {
        fun error() = SearchResult(emptyList(), 0, ERROR)

        fun success(items: List<ListItem>, totalResult: Int) = SearchResult(items, totalResult, LOADED)

        fun inProgress() = SearchResult(emptyList(), 0, IN_PROGRESS)
    }


}
