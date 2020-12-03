package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.User
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }

        init()
    }

    private fun init() {
        button_login.setOnClickListener {
            var email = edit_text_email_login.text.toString()
            var password = edit_text_password_login.text.toString()

            var intent = Intent(this, CartActivity::class.java)

            var params = HashMap<String, String>()
            // 여기 key 똑같아야함
            params["email"] = email
            params["password"] = password

            var jsonObject = JSONObject(params as Map<*, *>)

            var request = JsonObjectRequest(Request.Method.POST, Endpoints.getLogin(), jsonObject,
                Response.Listener {
                    Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()

                    var jsonObject = it.getJSONObject("user")
                    var name: String = jsonObject.getString("firstName")
                    var id: String = jsonObject.getString("_id")

                    sessionManager.login(id, name, email)

                    startActivity(intent)
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Login Fail", Toast.LENGTH_SHORT).show()
                    edit_text_email_login.setText("")
                    edit_text_password_login.setText("")
                })
            Volley.newRequestQueue(this).add(request)
        }

        text_view_new.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}