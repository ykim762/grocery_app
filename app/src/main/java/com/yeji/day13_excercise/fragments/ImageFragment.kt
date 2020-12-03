package com.yeji.day13_excercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AdapterProduct
import com.yeji.day13_excercise.adapters.SliderAdapter
import com.yeji.day13_excercise.models.Category
import kotlinx.android.synthetic.main.fragment_image.view.*

class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var image: String? = null
    lateinit var sliderAdapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getString("IMAGE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_image, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        Picasso
            .get()
            .load(image)
            .into(view.image_view_frag)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString("IMAGE", param1)
                }
            }
    }
}