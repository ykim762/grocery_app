package com.yeji.day13_excercise.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.OrderHistoryAdapter
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Order
import com.yeji.day13_excercise.models.OrderResponse
import kotlinx.android.synthetic.main.activity_user_account.*
import kotlinx.android.synthetic.main.app_bar.*

class UserAccountActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var userid: String
    lateinit var order: Order
    var mList: ArrayList<Order> = ArrayList()
    lateinit var orderAdapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)

        sessionManager = SessionManager(this)
        userid = sessionManager.getUser().id.toString()

        text_view_user_name.text = sessionManager.getUser().name
        text_view_user_email.text = sessionManager.getUser().email

        init()
    }

    private fun init() {
        getData()
        setUpToolbar()

        orderAdapter = OrderHistoryAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recycler_view_orders_account.layoutManager = linearLayoutManager
        recycler_view_orders_account.adapter = orderAdapter
    }

    private fun getData() {
        var request = StringRequest(
            Request.Method.GET, Endpoints.getOrdersByUserID(userid),
            Response.Listener {
                Log.d("abc", it.toString())
//                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()

                var gson = Gson()
                var orderResponse = gson.fromJson(it.toString(), OrderResponse::class.java)

                orderAdapter.setData(orderResponse.data)
                mList = orderResponse.data
            },
            Response.ErrorListener {
                Log.d("abc", "error:" + it.message)
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "User Account"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.menu_logout -> {
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                var sessionManager = SessionManager(this)
                sessionManager.logout()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return true
    }
}