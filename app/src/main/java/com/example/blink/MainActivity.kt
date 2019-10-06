package com.example.blink


import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var friendRecyclerView: androidx.recyclerview.widget.RecyclerView

    var friendTempList: MutableList<friend> = mutableListOf()
    lateinit var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager
    var fname: Array<String> = emptyArray()

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

        friendRecyclerView =
            findViewById(R.id.friend_recycler_view) as androidx.recyclerview.widget.RecyclerView
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        friendRecyclerView.layoutManager = layoutManager
        friendRecyclerView.setHasFixedSize(true)

        var count = 0;

        for (name: String in fname) {
            var c = friend(fname[count])
            friendTempList.add(c)
            count++
        }

        adapter = frAdapter(friendTempList, this)
        friendRecyclerView.adapter = adapter

        val data: Uri? = intent?.data

        val file: File = File(data?.path)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        var item: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView = item?.actionView as SearchView

        searchView.queryHint = "search friend"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        if (item!!.itemId == R.id.action_search) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
