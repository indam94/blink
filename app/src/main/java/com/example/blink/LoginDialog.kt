package com.example.blink

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess


class LoginDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)

    }

    private lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
                        textView.text = "Requesting.."
                        textView.setTextColor(Color.GRAY)

                        timer = Timer()
                        timer.schedule(sleep) {
                            if (s.isNullOrEmpty()) {
                                // do something

                                textView.text = s.toString()
                            }

                        }
                    }
                }

            }
        })

        customView = view

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

}