package com.yeji.day13_excercise.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.activities.PaymentActivity
import com.yeji.day13_excercise.database.DBAddressHelper
import com.yeji.day13_excercise.models.Address
import kotlinx.android.synthetic.main.row_address_adapter.view.*
import kotlinx.android.synthetic.main.row_cart_adapter.view.*

class AddressListAdapter(var mContext: Context) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    var mList:ArrayList<Address> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(address: Address){
            itemView.text_view_type_address.text = address.type.capitalize()
            itemView.text_view_city_address.text = address.city
            itemView.text_view_street_address.text = address.streetName + address.houseNo
            itemView.text_view_zipcode_address.text = address.pincode.toString()

            itemView.setOnClickListener {
                var intent = Intent(mContext, PaymentActivity::class.java)
                intent.putExtra(Address.KEY_ADDRESS, address)
                mContext.startActivity(intent)
            }

            itemView.image_button_delete_address.setOnClickListener {
                var dbAddressHolder = DBAddressHelper(mContext)
                dbAddressHolder.deleteAddress(address._id)
                delete(position)
            }

        }
    }

    fun delete(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    fun setData(list:ArrayList<Address>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_address_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var address = mList[position]
        holder.bind(address)
    }
}