package com.example.blink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Main2Activity : AppCompatActivity() {

    private lateinit var viewpager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initViews()

        setupViewPager()
//        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
//        viewpager_main.adapter = fragmentAdapter

//        tabs_main.setupWithViewPager(viewpager_main)
    }
    private fun initViews() {
        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
    }

    private fun setupViewPager() {
        val adapter = MyPagerAdapter(getSupportFragmentManager())

        var firstFragmet: MyFragment = MyFragment.newInstance("First Fragment")
        var secondFragmet: MyFragment = MyFragment.newInstance("Second Fragment")
        var thirdFragmet: MyFragment = MyFragment.newInstance("Third Fragment")

        adapter.addFragment(firstFragmet, "ONE")
        adapter.addFragment(secondFragmet, "TWO")
        adapter.addFragment(thirdFragmet, "THREE")

        viewpager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewpager)

    }
}
