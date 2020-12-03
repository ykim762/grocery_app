package com.yeji.day13_excercise.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.activities.SubCategoryActivity
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.models.Category
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.row_cat_adapter.view.*

class CategoryAdapter(var mContext:Context)
    :RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    var mList:ArrayList<Category> = ArrayList()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(category:Category){
            itemView.text_view_cat_row.text = category.catName

            Picasso
                .get()
                .load("${Config.IMAGE_URL + category.catImage}")
                .into(itemView.image_view_row)

            itemView.setOnClickListener{
                var intent = Intent(mContext, SubCategoryActivity::class.java)
                intent.putExtra(Category.KEY_CAT, category)

                mContext.startActivity(intent)
            }
        }
    }

    fun setData(list:ArrayList<Category>){
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_cat_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var category = mList[position]
        holder.bind(category)
    }

}