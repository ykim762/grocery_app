package com.yeji.day13_excercise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yeji.day13_excercise.fragments.ImageFragment
import com.yeji.day13_excercise.fragments.ProductListFragment
import com.yeji.day13_excercise.models.SubCategory

class SliderAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){

    var mList:ArrayList<String> = ArrayList()
    var mFragmentList:ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun setData(list:ArrayList<String>){
        mList = list
        notifyDataSetChanged()
    }

    fun addFragment(image: String){
        // subCat id 에 따라서
        mFragmentList.add(ImageFragment.newInstance(image))
    }

}