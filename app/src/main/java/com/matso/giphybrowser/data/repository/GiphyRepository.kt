package com.matso.giphybrowser.data.repository

import com.matso.giphybrowser.model.Giphy
import com.matso.giphybrowser.model.Result


interface GiphyRepository {
    suspend fun searchGiphies(term: String): Result<List<Giphy>>
    suspend fun requestMore(term: String): Result<List<Giphy>>
}
