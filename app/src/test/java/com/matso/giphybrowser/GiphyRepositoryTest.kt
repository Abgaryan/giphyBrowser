package com.matso.giphybrowser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.matso.giphybrowser.comman.ModelFactory.OFFSET
import com.matso.giphybrowser.comman.ModelFactory.OFFSET_2
import com.matso.giphybrowser.comman.ModelFactory.OFFSET_3
import com.matso.giphybrowser.comman.ModelFactory.SEARCH_TERM
import com.matso.giphybrowser.comman.ModelFactory.exception
import com.matso.giphybrowser.comman.ModelFactory.giphyList
import com.matso.giphybrowser.comman.ModelFactory.giphyList2
import com.matso.giphybrowser.comman.ModelFactory.searchResponse
import com.matso.giphybrowser.comman.ModelFactory.searchResponseWithOffset2
import com.matso.giphybrowser.comman.TestCoroutineRule
import com.matso.giphybrowser.data.api.GithyService
import com.matso.giphybrowser.data.repository.GiphyRepository
import com.matso.giphybrowser.data.repository.GiphyRepositoryImpl
import com.matso.giphybrowser.model.Failure
import com.matso.giphybrowser.model.Success
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 *  GiphyRepository unit test
 */
class GiphyRepositoryTest {


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var mockedGithyService: GithyService

    private lateinit var giphyRepository: GiphyRepository


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        runBlockingTest {
            coEvery { mockedGithyService.searchGiphy(SEARCH_TERM, OFFSET) } returns searchResponse
        }
        runBlockingTest {
            coEvery {
                mockedGithyService.searchGiphy(
                    SEARCH_TERM,
                    OFFSET_2
                )
            } returns searchResponseWithOffset2
        }
        giphyRepository = GiphyRepositoryImpl(mockedGithyService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSearchGiphies() {
        // test success
        val resultSuccess = runBlocking { giphyRepository.searchGiphies(SEARCH_TERM) }
        assertEquals(Success(giphyList), resultSuccess)

        // test failure
        runBlockingTest {
            coEvery { mockedGithyService.searchGiphy(SEARCH_TERM, OFFSET) } throws exception
        }
        val resultFailure = runBlocking { giphyRepository.searchGiphies(SEARCH_TERM) }
        assertEquals(Failure(exception), resultFailure)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testRequestMore() {
        clearMocks()

        // test success
        runBlocking { giphyRepository.searchGiphies(SEARCH_TERM) }
        val resultRequestedMore = runBlocking { giphyRepository.requestMore(SEARCH_TERM) }
        assertEquals(Success(giphyList2), resultRequestedMore)

        // test failure
        runBlockingTest {
            coEvery { mockedGithyService.searchGiphy(SEARCH_TERM, OFFSET_3) } throws exception
        }
        val resultFailure = runBlocking { giphyRepository.requestMore(SEARCH_TERM) }
        assertEquals(Failure(exception), resultFailure)
    }
}
