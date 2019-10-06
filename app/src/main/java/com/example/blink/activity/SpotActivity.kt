package com.example.blink.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.blink.fragments.ViewPagerAdapter
import com.example.blink.R
import com.example.blink.ReceiveRequest
import com.example.blink.fragments.SpotMapFragment
import com.google.android.material.tabs.TabLayout

class SpotActivity : AppCompatActivity() {

    private lateinit var viewpager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot)

        initViews()

        setupViewPager()
    }

    private fun initViews() {
        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(getSupportFragmentManager())

        var firstFragmet = SpotMapFragment()
        //var secondFragmet: ListFragment = ListFragment.newInstance("Second Fragment")

        adapter.addFragment(firstFragmet, "Search")
        //adapter.addFragment(secondFragmet, "List")

        viewpager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewpager)

    }

    private fun ReceiverMode(){
        while (true){
            Handler().postDelayed({

            }, 5000)
        }
    }
//
//    inner class ReceiverMode: Runnable{
//
//        var handler = Handler();
//
//        val r : Runnable = Runnable {
//
//        }
//
//        override fun run() {
//
//        }
//
//    }

}
