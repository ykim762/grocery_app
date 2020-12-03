package com.yeji.day13_excercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.models.Order
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.row_order_history_adapter.view.*

class OrderHistoryAdapter(var mContext:Context): RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){

    var mList:ArrayList<Order> = ArrayList()
    var pList:ArrayList<Product> = ArrayList()

    inner class ViewHolder(var itemview: View) : RecyclerView.ViewHolder(itemview){
        fun bind(order: Order){
            itemview.text_view_order_status.text = "Status: " + order.orderStatus
            itemview.text_view_order_date.text = "At " + order.date

            pList = order.products

            // products
            if (pList != null){
                var name: String = ""
                var quantity:String =""
                var price:String = ""
                for(i in pList.indices){
                    name += pList[i].productName + "\n"
                    quantity += "x" + pList[i].quantity + "\n"
                    price += "$" + pList[i].price + "\n"
                }
                itemview.text_view_product.text = name.substring(0, name.length-1)
                itemview.text_view_product_quantity_order.text = quantity.substring(0, quantity.length-1)
                itemview.text_view_product_price_order.text = price.substring(0, price.length-1)
            }

            if(order.shippingAddress != null){
                itemview.text_view_order_street_user.text = order.shippingAddress.streetName + " " + order.shippingAddress.houseNo
                itemview.text_view_order_city_user.text = order.shippingAddress.city
                itemview.text_view_order_zipcode_user.text = order.shippingAddress.pincode
            }


        }
    }

    fun setData(list: ArrayList<Order>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_order_history_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var order = mList[position]
        holder.bind(order)
    }
}