package com.yeji.day13_excercise.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.yeji.day13_excercise.R

class SplashActivity : AppCompatActivity() {

    private val delayedTime: Long = 2000  //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()
        handler.postDelayed({
            checkLogin()
        }, delayedTime)
    }

    private fun checkLogin() {
        var intent = if (true) {
            // user is logged in
            Intent(this, MainActivity::class.java)
        } else {
            // user not log in
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish() // destroy current activity
    }
}