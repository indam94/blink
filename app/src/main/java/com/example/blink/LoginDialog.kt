package com.example.blink

import android.app.Dialog
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

import kotlinx.android.synthetic.main.dialog_login.*

class LoginDialog : androidx.fragment.app.DialogFragment() {

    //private lateinit var customView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)

        var editText: EditText = view.findViewById(R.id.user_nickname_edit_text)
        var textView: TextView = view.findViewById(R.id.login_description_text_view)
        editText.addTextChangedListener(object : TextWatcher{
            var timer = Timer()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                timer.cancel()
                val sleep = 3000L
                when(s?.length){
                    1,2,3,4,5 -> {
                        textView.text = "nickname needs more than 5 characters"
                        textView.setTextColor(Color.RED)
                    }

                    else -> {

                        //Request
                        login_description_text_view.text = "Requesting.."
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

            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // Finish App
                exitProcess(-1)
            }.setCancelable(false)


        val dialog = builder.create()


        // optional
        dialog.setOnShowListener {
            // do something
        }

        return dialog
    }

    inner class CheckNickNameTask : AsyncTask<String, Boolean, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()

            val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)
            var textView: TextView = view.findViewById(R.id.login_description_text_view)
            Log.d("CheckNickName", "${textView.text}")
            textView.text = "Requesting......"
        }

        override fun doInBackground(vararg params: String?): Void? {

            var service: BlinkService = BlinkService.getInstance()
            var response = service.checkNickname(params[0]!!)

            Log.d("CheckNickName", "${response}")
            publishProgress(response)

            return null
        }

        override fun onProgressUpdate(vararg values: Boolean?) {
            super.onProgressUpdate(*values)

            Log.d("CheckNickName", "${values[0]!!}")

            val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)
            var textView: TextView = view.findViewById(R.id.login_description_text_view)

            when(values[0]!!){
                true->{
                    textView.text = "you can use this nickname."
                    textView.setTextColor(Color.GREEN)
                }
                false->{
                    textView.text = "this nickname is already used."
                    textView.setTextColor(Color.RED)
                }
            }
        }
    }
}