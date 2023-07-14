
package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search")
    val search: List<ListItem>?,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("Response")
    val response: String
) {
    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }

    fun hasResponse() = response == POSITIVE_RESPONSE

}
