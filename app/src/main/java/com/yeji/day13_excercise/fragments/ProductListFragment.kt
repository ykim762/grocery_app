package com.yeji.day13_excercise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AdapterProduct
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.models.ProductResponse
import com.yeji.day13_excercise.models.SubCategory
import kotlinx.android.synthetic.main.fragment_sub.*
import kotlinx.android.synthetic.main.fragment_sub.view.*

class ProductListFragment : Fragment() {
    private var subId: Int = 0
    lateinit var adapterProduct: AdapterProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(SubCategory.KEY_SUB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sub, container, false)

        initView(view)

        return view
    }

    private fun initView(view: View) {
        adapterProduct = AdapterProduct(activity!!)  // fragment라 activity
        view.recycler_view_sub.adapter = adapterProduct
        view.recycler_view_sub.layoutManager = LinearLayoutManager(activity)
        getData()
    }

    private fun getData() {
        var request = StringRequest(Request.Method.GET, Endpoints.getProductBySubID(subId),
            Response.Listener {
                progress_bar.visibility = View.GONE

                var gson = Gson()
                var productResponse = gson.fromJson(it, ProductResponse::class.java)
                adapterProduct.setData(productResponse.data)
            },
            Response.ErrorListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(activity).add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putInt(SubCategory.KEY_SUB_ID, param1)
                }
            }
    }
}