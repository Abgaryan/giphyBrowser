package com.matso.giphybrowser.ui.giphyserach

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matso.giphybrowser.Extensions.gone
import com.matso.giphybrowser.Extensions.snackbar
import com.matso.giphybrowser.Extensions.subscribe
import com.matso.giphybrowser.Extensions.visible
import com.matso.giphybrowser.R
import com.matso.giphybrowser.model.Giphy
import com.matso.giphybrowser.ui.base.Error
import com.matso.giphybrowser.ui.base.Loading
import com.matso.giphybrowser.ui.base.Success
import com.matso.giphybrowser.ui.base.ViewState
import com.matso.giphybrowser.ui.giphydetail.GiphyDetailFragment
import kotlinx.android.synthetic.main.fragment_giphy_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class GiphySearchFragment : Fragment(R.layout.fragment_giphy_list),
    OnListFragmentInteractionListener {

    companion object {
        private const val LAST_SEARCH_TERM: String = "last_search_term"
        private const val FRAGMENT_TAG = "fragment_tag"
        private const val BACKSTACK_TAG = "search_tag"
    }

    private val viewModel by viewModel<GiphySearchViewModel>()

    private val columnCount = 2

    private lateinit var emptyView: View


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = savedInstanceState?.getString(LAST_SEARCH_TERM)
        query?.let { viewModel.onSearchTermChanged(it) }
        viewReady()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_TERM, viewModel.lastQueryValue())
    }

    private fun viewReady() {
        emptyView = stubEmptyView.inflate()
        initAdapter()
        initSearch()
        setupScrollListener()
        subscribeToData()
    }

    private fun initSearch() =
        searchET.addTextChangedListener { s -> s?.let { viewModel.onSearchTermChanged(it) } }


    private fun initAdapter() {
        val giphyAdapter = GiphyRecyclerViewAdapter(this)
        with(giphyRecyclerView) {
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = giphyAdapter
            // add dividers between RecyclerView's  items
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }

    }

    private fun subscribeToData() {
        viewModel.getViewState.subscribe(viewLifecycleOwner, ::handleViewState)
    }

    private fun handleViewState(viewState: ViewState<List<Giphy>>) {
        when (viewState) {
            is Loading -> progress_circular.visible()
            is Success -> {
                progress_circular.gone()
                showHideEmptyView(viewState.data.isEmpty())
                showData(viewState.data)
            }
            is Error -> {
                progress_circular.gone()
                showError()
            }
        }
    }

    private fun showHideEmptyView(show: Boolean) {
        if (show) {
            emptyView.visible()
        } else {
            emptyView.gone()
        }
    }

    private fun showData(giphies: List<Giphy>) =
        (giphyRecyclerView.adapter as GiphyRecyclerViewAdapter).submitList(giphies.toMutableList())


    private fun showError() =
        snackbar(R.string.general_error_message, this.requireView())


    private fun setupScrollListener() {
        val layoutManager = giphyRecyclerView.layoutManager as LinearLayoutManager
        giphyRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }


    override fun onListFragmentInteraction(giphyUrl: String) = openGiphyDetailFragment(giphyUrl)

    private fun openGiphyDetailFragment(giphyUrl: String) {
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .replace(
                    R.id.details_fragment,
                    GiphyDetailFragment.newInstance(giphyUrl),
                    FRAGMENT_TAG
                )
                .addToBackStack(BACKSTACK_TAG)
                .commit()
        }
    }
}


