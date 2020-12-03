package com.yeji.day13_excercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.row_cart_adapter.view.*
import kotlinx.android.synthetic.main.row_payment_adapter.view.*

class PaymentAdapter(var mContext:Context):RecyclerView.Adapter<PaymentAdapter.ViewHolder>(){

    var mList:ArrayList<Product> = ArrayList()
    var listener:OnAdapterListener? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(product: Product, position: Int){
            itemView.text_view_name_payment.text = product.productName
            itemView.text_view_price_payment.text = "$" + product.price.toString()
            itemView.text_view_quantity_payment.text = "x" + product.quantity.toString()

            Picasso
                .get()
                .load("${Config.IMAGE_URL+product.image}")
                .into(itemView.image_view_payment)

            itemView.image_button_delete_payment.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
        }
    }

    fun setData(list:ArrayList<Product>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_payment_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: PaymentAdapter.ViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product, position)
    }

    fun delete(position:Int){
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(onAdapterListener: OnAdapterListener) {
        listener = onAdapterListener
    }
}