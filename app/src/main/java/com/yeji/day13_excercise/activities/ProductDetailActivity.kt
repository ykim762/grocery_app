package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Config
import com.yeji.day13_excercise.database.DBHelper
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.row_subcat_adapter.view.*

class ProductDetailActivity : AppCompatActivity() {

    lateinit var product:Product
    lateinit var sessionManager: SessionManager
    var mList:ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        sessionManager = SessionManager(this)
        product = intent.getSerializableExtra(Product.KEY_PRODUCT) as Product
        product.quantity = 1

        Picasso
            .get()
            .load("${Config.IMAGE_URL + product.image}")
            .into(image_view_detail)

        text_view_name_detail.text = product.productName
        text_view_detail.text = product.description

        var mrp: SpannableString = SpannableString("$" +product.mrp.toString())
        mrp.setSpan(StrikethroughSpan(), 0, mrp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text_view_mrp_detail.text = mrp
        text_view_price_detail.text = "$" + product.price.toString()

        init()
    }

    private fun init() {
        setUpToolbar()
        button_add_to.setOnClickListener{
            var dbHelper = DBHelper(this)
            if(dbHelper.isItemInCart(product)){
                product = dbHelper.getProductById(product._id)!!
            }
            dbHelper.addProduct(product)
            if(sessionManager.isLoggedIn()){

                startActivity(Intent(this, CartActivity::class.java))
            } else {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun setUpToolbar(){
        var toolbar = tool_bar
        toolbar.title = product.productName
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
            }
        }
        return true
    }

}