package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.ConfirmAdapter
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Order
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderConfirmActivity : AppCompatActivity() {

    lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        order = intent.getSerializableExtra(Order.KEY_ORDER) as Order

        var text_status = "Order " + order.orderStatus + "\n at " + order.date
        var text_sum = "\nSummary" + "\nPrice : $" + order.orderSummary.ourPrice + "\nDelivery fee : $" + order.orderSummary.deliveryCharges

        text_view_status_date.text = text_status
        text_view_order_sum.text = text_sum
        text_view_street_confirm.text = "Ship to \n" +order.shippingAddress.streetName + " " + order.shippingAddress.houseNo
        text_view_city_confirm.text = order.shippingAddress.city
        text_view_zipcode_confirm.text = order.shippingAddress.pincode

        init()
    }

    private fun init() {
        setUpToolbar()

        var confirmAdapter = ConfirmAdapter(this, order.products)

        recycler_view_confirm.adapter = confirmAdapter
        recycler_view_confirm.layoutManager = LinearLayoutManager(this)

        image_button_home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "Order Confirmation"
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