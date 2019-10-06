package com.example.blink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment(){

    private lateinit var bRecyclerView: RecyclerView
    private lateinit var bDownLoadProgressBar: ProgressBar
    private lateinit var bImageView:ImageView
    private lateinit var bNickNameTextView: TextView
    private lateinit var bListAdapter: ListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var listView : View = inflater.inflate(R.layout.fragment_list, null)

        bRecyclerView = listView.findViewById(R.id.list_recycler_view)

        //Row
        bDownLoadProgressBar = listView.findViewById(R.id.download_progressbar)
        bImageView = listView.findViewById(R.id.profile_imageview)
        bNickNameTextView = listView.findViewById(R.id.nick_name_textview)

        return view
    }
}