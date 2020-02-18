package com.matso.giphybrowser.comman

import com.matso.giphybrowser.model.*

object ModelFactory {
    const val SEARCH_TERM_P = "p"
    const val SEARCH_TERM_PI = "pi"

    const val SEARCH_TERM = "pizza"
    const val SEARCH_TERM_FAILURE = "pizza_PIZA"
    const val OFFSET = 1
    const val OFFSET_2 = 2
    const val OFFSET_3 = 3
    private val previewGif = PreviewGif(url = "url", width = 200, height = 200, size = 5000)
    private val images = Images(previewGif)
    private val giphy = Giphy("12", url = "url", images = images)
    val giphyList = listOf(giphy, giphy.copy(id = "12"))
    val giphyList2 = listOf(giphy, giphy.copy(id = "12"), giphy, giphy.copy(id = "12"))
    val pagination1 = Pagination(total_count = 120, count = 12, offset = 1)
    val searchResponse = SearchResponse(data = giphyList, pagination = pagination1)
    val searchResultSuccess = Success(giphyList)
    val searchResultSuccessWithOfset2 = Success(giphyList2)
    val searchResponseWithOffset2 =
        SearchResponse(data = giphyList, pagination = pagination1.copy(offset = OFFSET_2))
    val exception = Exception("Class not found exception")
}
