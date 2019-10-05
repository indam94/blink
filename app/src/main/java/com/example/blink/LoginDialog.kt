package com.example.blink

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_login.*
import kotlin.system.exitProcess

class LoginDialog : DialogFragment() {

    companion object {

        private const val FRAGMENT_TAG = "custom_dialog"

        fun newInstance() = LoginDialog()

        fun show(fragmentManager: FragmentManager): LoginDialog {
            val dialog = newInstance()
            // dialog.isCancelable = false
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }

    }

    private lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return customView//super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_login, null)
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
            }

        val dialog = builder.create()

        // optional
        dialog.setOnShowListener {
            // do something
        }

        return dialog

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //user_text.text = "hello"
    }


}