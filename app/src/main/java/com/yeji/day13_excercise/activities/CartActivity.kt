package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.CartAdapter
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_cart_adapter.view.*

class CartActivity : AppCompatActivity(), CartAdapter.OnAdapterListener {

    lateinit var dbHelper: DBHelper
    lateinit var cartadapter: CartAdapter
    var mList: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        init()
    }

    private fun init() {
        setUpToolbar()

        dbHelper = DBHelper(this)
        mList = dbHelper.readProduct()

        cartadapter = CartAdapter(this)
        cartadapter.setOnAdapterListener(this)
        cartadapter.setData(mList)

        if(!mList.isEmpty()){
            empty_text_view.visibility = View.GONE
        }

        recycler_view_cart.layoutManager = LinearLayoutManager(this)
        recycler_view_cart.adapter = cartadapter

        button_check_out.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }
        button_clear_cart.setOnClickListener {
            empty_text_view.visibility = View.VISIBLE
            dbHelper.clearCart()
            cartadapter.clear()
            updateUI()
        }
        updateUI()
    }

    override fun onButtonClicked(view: View, position: Int) {
        var product = mList[position]

        when (view.id) {
            R.id.image_button_delete -> {
                dbHelper.deleteProduct(product._id)
                cartadapter.delete(position)
                updateUI()
            }
            R.id.button_increase -> {
                product.quantity++
                dbHelper.updateQuantity(product._id, product.quantity)
                mList.set(position, product)
                cartadapter.notifyItemChanged(position, product)

                updateUI()
            }
            R.id.button_decrease -> {
                product.quantity--
                if (product.quantity == 0) {
                    dbHelper.deleteProduct(product._id)
                    cartadapter.delete(position)
                } else {
                    dbHelper.updateQuantity(product._id, product.quantity)
                    mList.set(position, product)
                    cartadapter.notifyItemChanged(position, product)
                }
                updateUI()
            }
        }
    }

    private fun updateUI() {
        text_view_cart_sub_total_num.text = "$" + calcSubTotal().toString()
        text_view_cart_discount_num.text = "- $" + calcDiscount().toString()
        text_view_cart_total_num.text = "$" + (calcSubTotal() - calcDiscount()).toString()
    }

    fun calcSubTotal(): Double {
        var sum = 0.0
        for (i in mList) {
            sum += i.mrp * i.quantity
        }
        return sum
    }

    fun calcDiscount(): Double {
        var sum = 0.0
        for (i in mList) {
            var discount = i.mrp - i.price
            sum += discount * i.quantity
        }
        return sum
    }

    fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 클릭되면 자동으로 불림
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cart -> {
                Toast.makeText(this, "cart", Toast.LENGTH_SHORT).show()
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