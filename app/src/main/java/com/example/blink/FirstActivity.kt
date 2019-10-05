package com.example.blink

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class FirstActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if(App.prefs.myUserName == "NOT_REG"){
            Toast.makeText(this,"You do not register nickname.", Toast.LENGTH_LONG).show()

            //Show Register Dialog
        }
        else{
            Toast.makeText(this, "You already have nickname.", Toast.LENGTH_LONG).show()
        }
    }

}