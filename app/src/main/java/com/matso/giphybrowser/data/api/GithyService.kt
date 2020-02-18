package com.matso.giphybrowser.data.api

import com.matso.giphybrowser.Constants.API_KEY
import com.matso.giphybrowser.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithyService {
    @GET("search?api_key=$API_KEY")
    suspend fun searchGiphy(@Query("q") term: String, @Query("offset") offset: Int): SearchResponse
}
