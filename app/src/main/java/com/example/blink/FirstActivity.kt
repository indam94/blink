package com.example.blink

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity




class FirstActivity: AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long=3000 // 3 sec

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if (App.prefs.myUserName != "X") {
            Toast.makeText(this,"You do not register nickname.", Toast.LENGTH_LONG).show()

            //need status Ok button is available
            val fm = supportFragmentManager
            val loginDialog = LoginDialog()
            loginDialog.isCancelable = false
            loginDialog.show(fm, "Simple Fragment")

        } else {
            Toast.makeText(this, "You already have nickname.", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                val nextIntent = Intent(this, Main2Activity::class.java)
                startActivity(nextIntent)

                finish()
            }, SPLASH_TIME_OUT)
        }


    }



}