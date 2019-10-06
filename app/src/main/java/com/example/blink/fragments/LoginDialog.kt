package com.example.blink.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.blink.App
import com.example.blink.R
import com.example.blink.activity.Main2Activity
import com.example.blink.utils.BlinkService
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

class LoginDialog : DialogFragment() {

    lateinit var rootView : View
    lateinit var loginTextView : TextView
    lateinit var mDialog: AlertDialog
    var canSignUp: Boolean = false
    var userNickName: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)
        rootView = view
        var editText: EditText = view.findViewById(R.id.user_nickname_edit_text)
        var textView: TextView = view.findViewById(R.id.login_description_text_view)
        loginTextView = textView
        editText.addTextChangedListener(object : TextWatcher {
            var timer = Timer()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                canSignUp = false
                timer.cancel()
                val sleep = 500L
                when (s?.length) {
                    1, 2, 3, 4, 5 -> {
                        textView.text = "nickname needs more than 5 characters"
                        textView.setTextColor(Color.RED)
                    }

                    else -> {

                        //Request
                        textView.text = "Requesting.."
                        textView.setTextColor(Color.GRAY)

                        timer = Timer()
                        timer.schedule(sleep) {
                            if (s.isNullOrEmpty()) {
                                // do something

                                //textView.text = s.toString()
                            }
                            //check NickName
                            var async = CheckNickNameTask()
                            async.execute(s.toString())


                        }
                    }
                }

            }
        })

        //customView = view

        val builder = AlertDialog.Builder(context!!)
            .setTitle("Sign Up NickName")
            .setView(view)
            .setPositiveButton(android.R.string.ok,null)
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // Finish App
                exitProcess(-1)
            }.setCancelable(false)


        mDialog = builder.create()

        // optional
        mDialog.setOnShowListener {
            // do something
            val okButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                // dialog won't close by default
                //dialog.dismiss()

                // Register NickName(-> sharedpreference)
                if(canSignUp){

                    //Submit
                    var async = SubmitNickName()
                    async.execute(userNickName)

                    //dialog.dismiss()
                }


            }
        }

        return mDialog
    }



    inner class CheckNickNameTask : AsyncTask<String, Boolean, Void>() {

        override fun doInBackground(vararg params: String?): Void? {

            var service: BlinkService = BlinkService.getInstance()
            Log.d("CheckNickName", "${service}")
            var response = service.checkNickname(params[0]!!)

            Log.d("CheckNickName", "${response}")
            userNickName = params[0]!!
            publishProgress(response)

            return null
        }

        override fun onProgressUpdate(vararg values: Boolean?) {
            super.onProgressUpdate(*values)

            Log.d("CheckNickName", "${values[0]!!}")

            when(values[0]!!){
                true->{
                    loginTextView.text = "you can use this nickname."
                    loginTextView.setTextColor(Color.GREEN)
                    canSignUp = true
                }
                false->{
                    loginTextView.text = "this nickname is already used."
                    loginTextView.setTextColor(Color.RED)
                    canSignUp = false
                }
            }
        }
    }

    inner class SubmitNickName: AsyncTask<String, Boolean, Void>(){
        override fun doInBackground(vararg params: String?): Void? {

            var service: BlinkService = BlinkService.getInstance()
            var response = service.submitNickname(params[0]!!)

            Log.d("SubmitNickName","${response}")

            publishProgress(response)

            return null
        }

        override fun onProgressUpdate(vararg values: Boolean?) {
            super.onProgressUpdate(*values)

            when(values[0]!!){
                true->{
                    App.prefs.myUserName = userNickName
                    mDialog.dismiss()
                    Toast.makeText(rootView.context, "submit success!", Toast.LENGTH_LONG).show()
                    val nextIntent = Intent(rootView.context, Main2Activity::class.java)
                    startActivity(nextIntent)
                }
                false->{
                    Toast.makeText(rootView.context, "fail submit, try again", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}