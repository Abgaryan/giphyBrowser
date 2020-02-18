package com.matso.giphybrowser.ui.giphydetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.matso.giphybrowser.R
import kotlinx.android.synthetic.main.fragment_githy_detail.*

private const val ARG_PARAM = "githyURL"

/**
 * Use the [GiphyDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiphyDetailFragment : Fragment(R.layout.fragment_githy_detail) {
    private var giphyURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            giphyURL = it.getString(ARG_PARAM)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        giphyURL?.let { Glide.with(this.requireContext()).asGif().load(it).into(giphyDetailsImV) }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param githyURL .
         * @return A new instance of fragment GithyDetailFragment.
         */
        @JvmStatic
        fun newInstance(githyURL: String) =
            GiphyDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, githyURL)
                }
            }
    }
}
