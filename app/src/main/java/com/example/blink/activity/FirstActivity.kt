package com.example.blink.activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blink.App
import com.example.blink.fragments.LoginDialogFragment
import com.example.blink.R
import com.example.blink.utils.BlinkService
import kotlinx.android.synthetic.main.ip_dialog.*


class FirstActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 sec

    fun proceed() {
        if (App.prefs.myUserName == "X") {
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val requiredPermissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        var permissionsToReq = ArrayList<String>()

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToReq.add(permission)
            }
        }

        if (permissionsToReq.size != 0) {
            val permArray = arrayOfNulls<String>(permissionsToReq.size)
            permissionsToReq.toArray(permArray)
            ActivityCompat.requestPermissions(this, permArray, 0)
        }

        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("IP ADDRESS")
        dialog.setMessage("Enter server IP address")
        dialog.setCancelable(false)
        var v1 = layoutInflater.inflate(R.layout.ip_dialog, null)
        dialog.setView(v1)
        dialog.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
            var d = dialog as AlertDialog;
            var editText = d.findViewById<EditText>(R.id.ip_address_edittext)
            App.prefs.myIp = editText.text.toString()
            BlinkService.getInstance()
            proceed()
        };
        dialog.show()
    }
}