package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.adapters.AddressListAdapter
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.database.DBAddressHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Address
import com.yeji.day13_excercise.models.User
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class NewAddressActivity : AppCompatActivity() {

    var mList:ArrayList<Address> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)

        init()
    }

    private fun init() {
        setUpToolbar()

        var sessionManager = SessionManager(this)

        button_submit_new_address.setOnClickListener {

            var radioGroup = findViewById<RadioGroup>(R.id.radio_group)
            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            var radioButton = findViewById<RadioButton>(intSelectButton)

            var type = radioButton.text.toString().toLowerCase()
            var street = edit_text_street_address.text.toString()
            var housenum = edit_text_housenum_address.text.toString()
            var city = edit_text_city_address.text.toString()
            var zipcode = edit_text_zipcode_address.text.toString()

            var params = HashMap<String, String>()
            // 여기 key 똑같아야함
            params["pincode"] = zipcode
            params["city"] = city
            params["streetName"] = street
            params["houseNo"] = housenum
            params["type"] = type
            params["userId"] = sessionManager.getUser().id.toString()

            var jsonObject = JSONObject(params as Map<*, *>)

            var request =
                JsonObjectRequest(Request.Method.POST, Endpoints.getAddress(), jsonObject,
                    Response.Listener {

                        var jsonObject = it.getJSONObject("data")
                        var id: String = jsonObject.getString("_id")

                        var address = Address(id, city, housenum, zipcode, street, type, sessionManager.getUser().id)

                        var dbAddressHelper = DBAddressHelper(this)
                        dbAddressHelper.addAddress(address)

                        mList.add(address)
                        var addressListAdapter = AddressListAdapter(this)
                        addressListAdapter.setData(mList)

//                        startActivity(Intent(this, AddressListActivity::class.java))
                        finish()
                    },
                    Response.ErrorListener {
                        Toast.makeText(applicationContext, "fail to add new address", Toast.LENGTH_SHORT).show()
                    })
            Volley.newRequestQueue(this).add(request)
        }

    }

    fun setUpToolbar(){
        var toolbar = tool_bar
        toolbar.title = "New Address"
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