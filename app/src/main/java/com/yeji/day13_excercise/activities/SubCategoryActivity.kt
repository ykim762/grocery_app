package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AdapterFragment
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Category
import com.yeji.day13_excercise.models.SubCategory
import com.yeji.day13_excercise.models.SubCategoryResponse
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*

class SubCategoryActivity : AppCompatActivity() {

    var category: Category? = null
    var sList: ArrayList<SubCategory> = ArrayList()
    lateinit var adapterFragment: AdapterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        category = intent.getSerializableExtra(Category.KEY_CAT) as Category

        init()

    }

    private fun init() {
        getData()
        setUpToolbar()

        adapterFragment = AdapterFragment(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
    }

    fun setUpToolbar(){
        var toolbar = tool_bar
        toolbar.title = category!!.catName
        setSupportActionBar(toolbar)
        // we want to have a back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 클릭되면 자동으로 불림
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.menu_logout -> {
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                var sessionManager = SessionManager(this)
                sessionManager.logout()
            }
        }
        return true
    }

    private fun getData() {
//        var requestQueue: RequestQueue = Volley.newRequestQueue(this)

        var request = StringRequest(
            Request.Method.GET, Endpoints.getSubCategoryByCatId(category!!.catId),
            Response.Listener {
                Log.d("abc", it.toString())

                var gson = Gson()
                var subcatResponse = gson.fromJson(it, SubCategoryResponse::class.java)
                sList.addAll(subcatResponse.data)
//                adapterSub.setData(subcatResponse.data)

                // insert fragment one by one
                for(i in 0 until sList.size){
                    adapterFragment.addFragment(sList[i])
                }

                view_pager.adapter = adapterFragment
                tab_layout.setupWithViewPager(view_pager)
            },
            Response.ErrorListener {
                Log.d("abc", "error:" + it.message)
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
//        requestQueue.add(request)
    }
}