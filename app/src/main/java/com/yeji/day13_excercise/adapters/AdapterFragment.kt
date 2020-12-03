package com.yeji.day13_excercise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yeji.day13_excercise.fragments.ProductListFragment
import com.yeji.day13_excercise.models.SubCategory

class AdapterFragment (fm:FragmentManager):FragmentPagerAdapter(fm) {

    var mFragmentList:ArrayList<Fragment> = ArrayList()
    var mTitleList:ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }

    fun addFragment(subCategory: SubCategory){
        // subCat id 에 따라서
        mFragmentList.add(ProductListFragment.newInstance(subCategory.subId))
        mTitleList.add(subCategory.subName)
    }
}