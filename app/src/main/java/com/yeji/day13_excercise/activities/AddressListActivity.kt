package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AddressListAdapter
import com.yeji.day13_excercise.database.DBAddressHelper
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Address
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.new_cart_layout.view.*

class AddressListActivity : AppCompatActivity() {

    lateinit var dbAddressHelper: DBAddressHelper
    var mList: ArrayList<Address> = ArrayList()
    lateinit var sessionManager: SessionManager
    var textViewCartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        init()
    }

    private fun init() {
        setUpToolbar()

        dbAddressHelper = DBAddressHelper(this)
        mList = dbAddressHelper.readAddress()
        sessionManager = SessionManager(this)

        var addressAdapter = AddressListAdapter(this)
        addressAdapter.setData(mList)

        // 추가한 후 돌아왔을때 어드레스 보이게
        recycler_view_address.layoutManager = LinearLayoutManager(this)
        recycler_view_address.adapter = addressAdapter

        button_new_address.setOnClickListener {
            startActivity(Intent(this, NewAddressActivity::class.java))
            finish()
        }

    }

    fun setUpToolbar() {
        var toolbar = tool_bar
        toolbar.title = "Address List"
        setSupportActionBar(toolbar)
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
            startActivity(Intent(this, CartActivity::class.java))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
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