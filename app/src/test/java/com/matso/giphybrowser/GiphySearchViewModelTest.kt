package com.matso.giphybrowser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.matso.giphybrowser.Constants.DEBOUNCE_TIMEOUT
import com.matso.giphybrowser.comman.ModelFactory
import com.matso.giphybrowser.comman.ModelFactory.SEARCH_TERM
import com.matso.giphybrowser.comman.ModelFactory.SEARCH_TERM_FAILURE
import com.matso.giphybrowser.comman.ModelFactory.SEARCH_TERM_P
import com.matso.giphybrowser.comman.ModelFactory.SEARCH_TERM_PI
import com.matso.giphybrowser.comman.ModelFactory.searchResultSuccess
import com.matso.giphybrowser.comman.ModelFactory.searchResultSuccessWithOfset2
import com.matso.giphybrowser.comman.TestCoroutineRule
import com.matso.giphybrowser.data.repository.GiphyRepository
import com.matso.giphybrowser.model.Failure
import com.matso.giphybrowser.model.Giphy
import com.matso.giphybrowser.schedulers.TrampolineSchedulerProvider
import com.matso.giphybrowser.ui.base.Error
import com.matso.giphybrowser.ui.base.Success
import com.matso.giphybrowser.ui.base.ViewState
import com.matso.giphybrowser.ui.giphyserach.GiphySearchViewModel
import com.matso.livedatatest.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 *  GiphySearchViewModelTest unit test
 */
class GiphySearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()


    @RelaxedMockK
    private lateinit var giphyRepository: GiphyRepository

    private val schedulerProvider = TrampolineSchedulerProvider()

    private lateinit var viewModel: GiphySearchViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        runBlockingTest {
            coEvery { giphyRepository.searchGiphies(SEARCH_TERM) } returns searchResultSuccess
            coEvery { giphyRepository.searchGiphies(SEARCH_TERM_P) } returns searchResultSuccessWithOfset2
            coEvery { giphyRepository.searchGiphies(SEARCH_TERM_PI) } returns searchResultSuccessWithOfset2
        }

        runBlockingTest {
            coEvery { giphyRepository.searchGiphies(SEARCH_TERM_FAILURE) } returns Failure(
                ModelFactory.exception
            )
        }

        viewModel = GiphySearchViewModel(giphyRepository, schedulerProvider)

    }


    @ExperimentalCoroutinesApi
    @Test
    fun testViewStateSuccess() {
        viewModel.onSearchTermChanged(SEARCH_TERM)
        Thread.sleep(2 * DEBOUNCE_TIMEOUT)
        val expectedViewState: ViewState<List<Giphy>> = Success(searchResultSuccess.data)
        viewModel.getViewState.test().assertValue(expectedViewState)
    }

    @ExperimentalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testViewStateError() {
        viewModel.onSearchTermChanged(SEARCH_TERM_FAILURE)
        Thread.sleep(2 * DEBOUNCE_TIMEOUT)
        assertEquals(viewModel.lastQueryValue(), SEARCH_TERM_FAILURE)
        val expectedViewState: ViewState<List<Giphy>> = Error(ModelFactory.exception)
        viewModel.getViewState.test().assertValue(expectedViewState)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testViewStateLoading() {
        viewModel.onSearchTermChanged(SEARCH_TERM_P)
        viewModel.onSearchTermChanged(SEARCH_TERM_PI)
        viewModel.onSearchTermChanged(SEARCH_TERM)
        Thread.sleep(3 * DEBOUNCE_TIMEOUT)
        viewModel.getViewState.test().assertHasValue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSearchDebounce() {
        viewModel.onSearchTermChanged(SEARCH_TERM_P)
        viewModel.onSearchTermChanged(SEARCH_TERM_PI)
        viewModel.onSearchTermChanged(SEARCH_TERM)
        Thread.sleep(3 * DEBOUNCE_TIMEOUT)
        viewModel.getViewState.test().assertValue(Success(searchResultSuccess.data))
    }


}

