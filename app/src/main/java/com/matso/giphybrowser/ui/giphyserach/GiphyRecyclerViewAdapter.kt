package com.matso.giphybrowser.ui.giphyserach

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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


    private val clickListener: View.OnClickListener

    init {
        clickListener = View.OnClickListener { v ->
            val item = v.tag as Giphy
            listener?.onListFragmentInteraction(item.images.previewGif.url)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_giphy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        Glide.with(holder.giphyImV.context).asGif().load(item.images.previewGif.url)
            .into(holder.giphyImV)
        with(holder.view) {
            tag = item
            setOnClickListener(clickListener)
        }
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val giphyImV: ImageView = view.giphyImV
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
