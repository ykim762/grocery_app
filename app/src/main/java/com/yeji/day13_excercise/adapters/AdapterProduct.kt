package com.yeji.day13_excercise.adapters

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.activities.ProductDetailActivity
import com.yeji.day13_excercise.activities.SubCategoryActivity
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.models.Category
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.row_subcat_adapter.view.*

class AdapterProduct (var mContext:Context):RecyclerView.Adapter<AdapterProduct.ViewHolder>(){

    var mList:ArrayList<Product> = ArrayList()

    inner class ViewHolder(itemVeiw:View): RecyclerView.ViewHolder(itemVeiw){
        fun bind(product: Product){
            itemView.text_view_name_sub.text = product.productName
            itemView.text_view_price_sub.text = "$"+product.price.toString()
            var mrp:SpannableString = SpannableString("$" +product.mrp.toString())
            mrp.setSpan(StrikethroughSpan(), 0, mrp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemView.text_view_mrp_sub.text = mrp

            Picasso
                .get()
                .load("${Config.IMAGE_URL+product.image}")
                .into(itemView.image_view_sub)

            itemView.setOnClickListener{
                var intent = Intent(mContext, ProductDetailActivity::class.java)
                intent.putExtra(Product.KEY_PRODUCT, product)

                mContext.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_subcat_adapter, parent, false)
        return ViewHolder(view)
    }

    fun setData(list:ArrayList<Product>){
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product)
    }

}