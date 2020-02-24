package com.matso.giphybrowser.ui.giphyserach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matso.giphybrowser.R
import com.matso.giphybrowser.model.Giphy
import kotlinx.android.synthetic.main.fragment_giphy.view.*


class GiphyRecyclerViewAdapter(
    private val listener: OnListFragmentInteractionListener?
) : ListAdapter<Giphy, GiphyRecyclerViewAdapter.ViewHolder>(GIPHY_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_giphy, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(val view: View, itemClickListener: OnListFragmentInteractionListener?) :
        RecyclerView.ViewHolder(view) {
        private var giphy: Giphy? = null

        init {
            view.setOnClickListener { _ ->
                giphy?.let {
                    itemClickListener?.onListFragmentInteraction(it.images.previewGif.url)
                }
            }
        }

        fun bind(item: Giphy) = with(itemView) {
            giphy = item
            Glide.with(giphyImV.context).asGif().load(item.images.previewGif.url)
                .into(giphyImV)

        }
    }

    companion object {

        private val GIPHY_COMPARATOR = object : DiffUtil.ItemCallback<Giphy>() {

            override fun areItemsTheSame(oldItem: Giphy, newItem: Giphy): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Giphy, newItem: Giphy): Boolean =
                oldItem == newItem
        }
    }

}
