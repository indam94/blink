package com.example.blink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var friendRecyclerView: androidx.recyclerview.widget.RecyclerView


    var friendTempList: MutableList<friend> = mutableListOf()
    lateinit var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager
    var fname: Array<String> = arrayOf("Harry", "Heo", "Lee")

    lateinit var adapter: frAdapter


//    // The path to the root of this app's internal storage
//    private lateinit var privateRootDir: File
//    // The path to the "images" subdirectory
//    private lateinit var imagesDir: File
//    // Array of files in the images subdirectory
//    private lateinit var imageFiles: Array<File>
//    // Array of filenames corresponding to imageFiles
//    private lateinit var imageFilenames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        friendRecyclerView = findViewById(R.id.friend_recycler_view) as androidx.recyclerview.widget.RecyclerView
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
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

        // val data: Uri? = intent?.data

        // Figure out what to do based on the intent type
        if (intent?.type?.startsWith("image/") == true) {
            // Handle intents with image data ...

        } else if (intent?.type == "text/plain") {
            // Handle intents with text ...

        }
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
