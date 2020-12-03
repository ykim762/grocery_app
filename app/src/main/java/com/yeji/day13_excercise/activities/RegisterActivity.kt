package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.yeji.day13_excercise.R
import com.yeji.day13_excercise.app.Endpoints
import com.yeji.day13_excercise.helper.SessionManager
import com.yeji.day13_excercise.models.User
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        button_submit_reg.setOnClickListener {
            var name = edit_text_name_reg.text.toString()
            var email = edit_text_email_reg.text.toString()
            var password = edit_text_password_reg.text.toString()
            var mobile = edit_text_mobile_reg.text.toString()

            var sessionManager = SessionManager(this)

            // need to convert as json
            var params = HashMap<String, String>()
            // 여기 key 똑같아야함
            params["firstName"] = name
            params["email"] = email
            params["password"] = password
            params["mobile"] = mobile

            var jsonObject = JSONObject(params as Map<*, *>)

            //int method, String url, JSONObject jsonRequest, Listener<JSONObject>, ErrorListener
            var request =
                JsonObjectRequest(Request.Method.POST, Endpoints.getRegister(), jsonObject,
                    Response.Listener {
                        Toast.makeText(applicationContext, "Register Success", Toast.LENGTH_SHORT)
                            .show()

                        var jsonObject = it.getJSONObject("data")
                        var id: String = jsonObject.getString("_id")

                        var user = User(id, name, email, password)

                        sessionManager.register(user)

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    },
                    Response.ErrorListener {
                        Toast.makeText(applicationContext, "Register Fail", Toast.LENGTH_SHORT)
                            .show()
                    })
            Volley.newRequestQueue(this).add(request)
        }

        text_view_already.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}