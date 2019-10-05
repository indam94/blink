package com.example.blink

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlin.system.exitProcess

class FirstActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if(App.prefs.myUserName == "X"){
            Toast.makeText(this,"You do not register nickname.", Toast.LENGTH_LONG).show()

            //Show Login Dialog
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Sign Up NickName")
            builder.setCancelable(false)
            builder.setIcon(R.mipmap.ic_launcher)

            //need status Ok button is available



//            var v1 = layoutInflater.inflate(R.layout.dialog_login, null)
//            builder.setView(v1)
//
//            var listener = object:DialogInterface.OnClickListener{
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//
//                    when(which){
//                        DialogInterface.BUTTON_POSITIVE->{
//                            //save sharedpreferences
//
//                        }
//                        DialogInterface.BUTTON_NEGATIVE->{
//                            exitProcess(-1)
//                        }
//                    }
//
//                }
//            }
//
//            builder.setPositiveButton("OK", listener)
//            builder.setNegativeButton("NO", listener)
//
//            builder.show()
        }
        else{

            Toast.makeText(this, "You already have nickname.", Toast.LENGTH_LONG).show()
        }
    }

}