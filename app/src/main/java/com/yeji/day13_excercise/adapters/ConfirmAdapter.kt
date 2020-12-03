package com.yeji.day13_excercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.row_cofirm_adapter.view.*
import kotlinx.android.synthetic.main.row_payment_adapter.view.*

class ConfirmAdapter(var mContext:Context, var mList:ArrayList<Product>)
    :RecyclerView.Adapter<ConfirmAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product){
            itemView.text_view_name_confirm.text = product.productName
            itemView.text_view_price_confirm.text = "$" + product.price.toString()
            itemView.text_view_quantity_confirm.text = "x" + product.quantity.toString()

            Picasso
                .get()
                .load("${Config.IMAGE_URL+product.image}")
                .into(itemView.image_view_confirm)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_cofirm_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product)
    }

}