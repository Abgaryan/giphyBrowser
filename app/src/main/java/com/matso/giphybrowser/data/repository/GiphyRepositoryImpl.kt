package com.matso.giphybrowser.data.repository

import com.matso.giphybrowser.data.api.GithyService
import com.matso.giphybrowser.model.*


class GiphyRepositoryImpl(private val service: GithyService) :
    GiphyRepository {

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<Giphy>()


    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override suspend fun searchGiphies(term: String): Result<List<Giphy>> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        return requestData(term)
    }

    override suspend fun requestMore(term: String) = requestData(term)

    private suspend fun requestData(term: String): Result<List<Giphy>> {
        if (isRequestInProgress) return Canceled<Nothing>()
        isRequestInProgress = true
        return try {
            val searchResponse = service.searchGiphy(term, lastRequestedPage)
            lastRequestedPage++
            isRequestInProgress = false
            inMemoryCache.addAll(searchResponse.data)
            Success(inMemoryCache)
        } catch (e: Exception) {
            isRequestInProgress = false
            Failure(e)
        }
    }


}
