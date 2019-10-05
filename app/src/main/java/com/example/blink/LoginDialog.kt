package com.example.blink

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

class LoginDialog : DialogFragment() {

    lateinit var rootView : View
    lateinit var loginTextView : TextView
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
                val sleep = 3000L
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
            .setTitle("Custom Dialog")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // Register NickName(-> sharedpreference)
                if(canSignUp){
                    App.prefs.myUserName = userNickName
                }
                else{

                }

            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // Finish App
                exitProcess(-1)
            }.setCancelable(false)


        val dialog = builder.create()


        // optional
        dialog.setOnShowListener {
            // do something
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                // dialog won't close by default
                dialog.dismiss()
            }
        }

        return dialog
    }



    inner class CheckNickNameTask : AsyncTask<String, Boolean, Void>() {

        override fun doInBackground(vararg params: String?): Void? {

            var service: BlinkService = BlinkService.getInstance()
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
}