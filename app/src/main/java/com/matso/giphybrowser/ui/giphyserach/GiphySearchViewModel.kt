package com.matso.giphybrowser.ui.giphyserach

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matso.giphybrowser.Constants.DEBOUNCE_TIMEOUT
import com.matso.giphybrowser.data.repository.GiphyRepository
import com.matso.giphybrowser.model.Giphy
import com.matso.giphybrowser.model.onFailure
import com.matso.giphybrowser.model.onSuccess
import com.matso.giphybrowser.schedulers.BaseSchedulerProvider
import com.matso.giphybrowser.ui.base.Error
import com.matso.giphybrowser.ui.base.Loading
import com.matso.giphybrowser.ui.base.Success
import com.matso.giphybrowser.ui.base.ViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class GiphySearchViewModel(
    private val repository: GiphyRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 10
    }

    private val disposables = CompositeDisposable()
    private val subject = PublishSubject.create<String>()

    private val queryLiveData = MutableLiveData<String>()


    @VisibleForTesting
    val viewState = MutableLiveData<ViewState<List<Giphy>>>()

    val getViewState: LiveData<ViewState<List<Giphy>>>
        get() = viewState

    init {
        disposables.add(
            subject
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .map { text -> text.trim() }
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe { term ->
                    queryLiveData.postValue(term)
                    onSearch(term)
                })

    }

    fun onSearchTermChanged(s: CharSequence) = subject.onNext(s.toString())


    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val queryTerm = lastQueryValue()
            queryTerm?.let { term ->
                requestMore(term)
            }

        }
    }

    @VisibleForTesting
    fun requestMore(term: String) {
        viewModelScope.launch {
            viewState.value = Loading()
            repository.requestMore(term) //
                .onSuccess {
                    viewState.value = Success(it)
                } //
                .onFailure {
                    viewState.value = Error(it)
                }
        }
    }

    private fun onSearch(term: String) {
        viewModelScope.launch {
            viewState.value = Loading()
            repository.searchGiphies(term)
                .onSuccess {
                    viewState.value = Success(it)
                } //
                .onFailure {
                    viewState.value = Error(it)
                }
        }
    }


    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
