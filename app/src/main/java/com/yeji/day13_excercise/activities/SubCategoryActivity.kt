package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AdapterFragment
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Category
import com.yeji.day13_excercise.models.SubCategory
import com.yeji.day13_excercise.models.SubCategoryResponse
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.new_cart_layout.view.*

class SubCategoryActivity : AppCompatActivity() {

    var category: Category? = null
    var sList: ArrayList<SubCategory> = ArrayList()
    lateinit var adapterFragment: AdapterFragment

    lateinit var sessionManager:SessionManager
    var textViewCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        category = intent.getSerializableExtra(Category.KEY_CAT) as Category

        init()

    }

    private fun init() {
        getData()
        setUpToolbar()

        sessionManager = SessionManager(this)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // cart
        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.new_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        textViewCartCount = view.text_view_cart_count

        updateUI()

        view.setOnClickListener {
            if(sessionManager.isLoggedIn()) {
                startActivity(Intent(this, CartActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun updateUI() {
        var dbHelper = DBHelper(this)
        var count = dbHelper.getCartTotalQty()
        if (count == 0) {
            textViewCartCount?.visibility = View.GONE

        } else {
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = count.toString()
        }
    }

    // 클릭되면 자동으로 불림
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_logout -> {
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
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