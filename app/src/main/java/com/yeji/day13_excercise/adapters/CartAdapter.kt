package com.yeji.day13_excercise.adapters

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.row_cart_adapter.view.*
import kotlinx.android.synthetic.main.row_subcat_adapter.view.*

class CartAdapter(var mcontext: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var mList: ArrayList<Product> = ArrayList()
    var listener: OnAdapterListener? = null
    var dbHelper = DBHelper(mcontext)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, position: Int) {
            itemView.text_view_name_cart.text = product.productName
            itemView.text_view_price_cart.text = "$" + product.price.toString()

            var mrp: SpannableString = SpannableString("$" + product.mrp.toString())
            mrp.setSpan(StrikethroughSpan(), 0, mrp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            itemView.text_view_mrp_cart.text = mrp
            itemView.text_view_quantity.text = product.quantity.toString()

            Picasso
                .get()
                .load("${Config.IMAGE_URL + product.image}")
                .into(itemView.image_view_cart)

            itemView.image_button_delete.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
            itemView.button_increase.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
            itemView.button_decrease.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
        }
    }

    fun setData(list: ArrayList<Product>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mcontext).inflate(R.layout.row_cart_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product, position)
    }

    fun delete(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.removeAll(mList)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(onAdapterListener: OnAdapterListener) {
        listener = onAdapterListener
    }
}