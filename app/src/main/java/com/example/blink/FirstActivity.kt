package com.example.blink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast


class FirstActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if(App.prefs.myUserName == "X"){
            Toast.makeText(this,"You do not register nickname.", Toast.LENGTH_LONG).show()

            //need status Ok button is available
            val fm = supportFragmentManager
            val loginDialog = LoginDialog()
            loginDialog.isCancelable = false
            loginDialog.show(fm, "Simple Fragment")

        }
        else{
            //call bucket map view

        }


    }

}