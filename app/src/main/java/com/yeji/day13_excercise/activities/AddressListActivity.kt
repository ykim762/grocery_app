package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AddressListAdapter
import com.yeji.day13_excercise.database.DBAddressHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Address
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.app_bar.*

class AddressListActivity : AppCompatActivity() {

    lateinit var dbAddressHelper: DBAddressHelper
    var mList:ArrayList<Address> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        init()
    }

    private fun init() {
        setUpToolbar()

        dbAddressHelper = DBAddressHelper(this)
        mList = dbAddressHelper.readAddress()

        var addressAdapter = AddressListAdapter(this)
        addressAdapter.setData(mList)

        // 추가한 후 돌아왔을때 어드레스 보이게
        recycler_view_address.layoutManager = LinearLayoutManager(this)
        recycler_view_address.adapter = addressAdapter

        button_new_address.setOnClickListener {
            startActivity(Intent(this, NewAddressActivity::class.java))
        }

    }

    fun setUpToolbar(){
        var toolbar = tool_bar
        toolbar.title = "Address List"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

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
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return true
    }
}