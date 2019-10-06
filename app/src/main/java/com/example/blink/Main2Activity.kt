package com.example.blink

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.io.File

class Main2Activity : AppCompatActivity() {

    private lateinit var viewpager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initViews()

        setupViewPager()
    }

    private fun initViews() {
        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
    }

    private fun setupViewPager() {
        val adapter = MyPagerAdapter(getSupportFragmentManager())

        var firstFragmet: MapFragment = MapFragment()
        //var secondFragmet: ListFragment = ListFragment.newInstance("Second Fragment")

        adapter.addFragment(firstFragmet, "Search")
        //adapter.addFragment(secondFragmet, "List")

        viewpager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewpager)

    }


}
