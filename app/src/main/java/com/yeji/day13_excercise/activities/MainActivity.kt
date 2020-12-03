package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.CategoryAdapter
import com.yeji.day13_excercise.adapters.SliderAdapter
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Category
import com.yeji.day13_excercise.models.CategoryResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    var mList: ArrayList<Category> = ArrayList()
    var imageList: ArrayList<String> = ArrayList()
    lateinit var adapterCategory: CategoryAdapter
    lateinit var adapterSlider: SliderAdapter
    lateinit var sessionManager: SessionManager

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        init()
    }

    private fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "Grocery"
//        toolbar.subtitle = "This Grocery App"
        setSupportActionBar(toolbar)
//        supportActionBar
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 클릭되면 자동으로 불림
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_cart -> {
                if(sessionManager.isLoggedIn()) {
                    startActivity(Intent(this, CartActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            R.id.menu_logout -> {
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                var sessionManager = SessionManager(this)
                sessionManager.logout()
            }
        }
        return true
    }

    private fun init() {
        getData()
        setUpToolbar()

        //navigation view
        drawerLayout = drawer_layout
        navView = nav_view
        var headerview = navView.getHeaderView(0)
        headerview.text_view_name_nav_header.text = sessionManager.getUser().name
        headerview.text_view_email_nav_header.text = sessionManager.getUser().email

        var toggle = ActionBarDrawerToggle(this, drawerLayout, tool_bar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        adapterCategory = CategoryAdapter(this)
        recycler_view.layoutManager = GridLayoutManager(this, 2)
//        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter = adapterCategory
        adapterSlider = SliderAdapter(supportFragmentManager)
        image_view_pager.adapter = adapterSlider
    }

    private fun getData() {
//        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(Request.Method.GET, Endpoints.getCategory(),
                Response.Listener {
                    Log.d("abc", it.toString())

                    var gson = Gson()
                    var catResponse = gson.fromJson(it.toString(), CategoryResponse::class.java)
                    adapterCategory.setData(catResponse.data)
                mList = catResponse.data

                for (i in mList.indices) {
                    imageList.add("${Config.IMAGE_URL + mList[i].catImage}")
                }

                // insert fragment one by one
                for(i in 0 until imageList.size){
                    adapterSlider.addFragment(imageList[i])
                }

                adapterSlider.setData(imageList)
            },
            Response.ErrorListener {
                Log.d("abc", "error:" + it.message)
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_LONG).show()
            })

//        requestQueue.add(request)
        Volley.newRequestQueue(this).add(request)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_account -> {
                startActivity(Intent(this, UserAccountActivity::class.java))
            }
            R.id.item_logout -> {
                sessionManager.logout()
                Toast.makeText(applicationContext, "Logout", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // 네비 바 열려있을때 백 버튼 눌러도 네비만 끄게
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }
}