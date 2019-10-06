package com.example.blink.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blink.App
import com.example.blink.fragments.LoginDialogFragment
import com.example.blink.R


class FirstActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 sec

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if (App.prefs.myUserName != "X") {
            Toast.makeText(this, "You do not register nickname.", Toast.LENGTH_LONG).show()

            //need status Ok button is available
            val fm = supportFragmentManager
            val loginDialog = LoginDialogFragment()
            loginDialog.isCancelable = false
            loginDialog.show(fm, "Simple Fragment")

        } else {
            Toast.makeText(this, "You already have nickname.", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                val nextIntent = Intent(this, SpotActivity::class.java)
                startActivity(nextIntent)

                finish()
            }, SPLASH_TIME_OUT)
        }


    }


}