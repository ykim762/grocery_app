package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AddressListAdapter
import com.yeji.day13_excercise.adapters.CartAdapter
import com.yeji.day13_excercise.adapters.PaymentAdapter
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.database.DBAddressHelper
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Address
import com.yeji.day13_excercise.models.Order
import com.yeji.day13_excercise.models.OrderSummary
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentAdapter.OnAdapterListener {

    lateinit var dbHelper: DBHelper
    lateinit var payAdapter: PaymentAdapter
    var mList: ArrayList<Product> = ArrayList()
    lateinit var address: Address
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address

        text_view_street_payment.text = address.streetName + address.houseNo
        text_view_city_payment.text = address.city
        text_view_zipcode_payment.text = address.pincode

        init()
        updateUI()
    }

    private fun init() {
        setUpToolbar()

        dbHelper = DBHelper(this)
        mList = dbHelper.readProduct()

        payAdapter = PaymentAdapter(this)
        payAdapter.setData(mList)

        recycler_view_payment.layoutManager = LinearLayoutManager(this)
        recycler_view_payment.adapter = payAdapter

        sessionManager = SessionManager(this)

        button_pay_with_cash.setOnClickListener {

            // request order
            var userid = sessionManager.getUser().id.toString()

            var orderSum = OrderSummary(userid, 0, calcDiscount(), mList.size, calcTotal(), calcAmount())

            var status = "Confirmed"

            var order = Order(orderStatus = status, orderSummary = orderSum, products = mList, shippingAddress = address, user = sessionManager.getUser(), userId = userid )

            var jsonString = Gson().toJson(order)
            var jsonObject = JSONObject(jsonString)

            var request = JsonObjectRequest(Request.Method.POST, Endpoints.postOrders(), jsonObject,
                Response.Listener {

                    var jsonObj = it.getJSONObject("data")
                    var id: String = jsonObj.getString("_id")
                    var date: String = jsonObj.getString("date")
                    var v: Int = jsonObj.getString("__v").toInt()

                    var sOrder = Order(v,id,date,status,orderSum, mList, address, sessionManager.getUser(), userid)

                    var intent = Intent(this, OrderConfirmActivity::class.java)
                    intent.putExtra(Order.KEY_ORDER, sOrder)
                    var anintent = Intent(this, UserAccountActivity::class.java)
                    intent.putExtra(Order.KEY_ORDER, sOrder)
                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "fail to order address", Toast.LENGTH_SHORT)
                        .show()
                })
            Volley.newRequestQueue(this).add(request)
        }
    }

    fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "Payment Summary"
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

    override fun onButtonClicked(view: View, position: Int) {
        var product = mList[position]
        when (view.id) {
            R.id.image_button_delete_payment -> {
                dbHelper.deleteProduct(product._id)
                payAdapter.delete(position)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        text_view_payment_total_num.text = "$" + calcTotal().toString()
    }

    fun calcDiscount(): Double {
        var sum = 0.0
        for (i in mList) {
            var discount = i.mrp - i.price
            sum += discount * i.quantity
        }
        return sum
    }

    fun calcAmount(): Int {
        var sum = 0
        for (i in mList) {
            sum += i.quantity
        }
        return sum
    }

    fun calcTotal(): Double {
        var sum = 0.0
        for (i in mList) {
            sum += i.price * i.quantity
        }
        return sum
    }
}