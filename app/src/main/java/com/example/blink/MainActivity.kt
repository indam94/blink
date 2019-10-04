package com.example.blink

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    lateinit var friendRecyclerView: RecyclerView


    var friendTempList: MutableList<friend> = mutableListOf()
    lateinit var layoutManager: RecyclerView.LayoutManager
    var fname: Array<String> = arrayOf("Harry", "Heo", "Lee")

    lateinit var adapter: frAdapter

    //lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        friendRecyclerView = findViewById(R.id.friend_recycler_view) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        friendRecyclerView.layoutManager = layoutManager
        friendRecyclerView.setHasFixedSize(true)

        var count = 0;

        for(name: String in fname){
            var c : friend = friend(fname[count])
            friendTempList.add(c)
            count++
        }

        adapter = frAdapter(friendTempList, this)
        friendRecyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        var item: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView = item?.actionView as SearchView

        searchView.queryHint = "search friend"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {

                adapter.filter.filter(p0)

                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
        })

        searchView.maxWidth = Int.MAX_VALUE

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.action_search){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
