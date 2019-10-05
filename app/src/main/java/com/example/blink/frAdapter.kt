package com.example.blink

import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.contracts.contract

class frAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<frAdapter.Companion.Holder>, Filterable{
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString: String = constraint.toString()

                if(charString.isEmpty()){
                    listFiltered = list
                }
                else{
                    var filteredList: MutableList<friend> = mutableListOf()
                    for(f: friend in list){
                        if(f.getFname().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(f)
                        }
                    }
                    listFiltered = filteredList
                }

                var filterResult: FilterResults = FilterResults()
                filterResult.values = listFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                listFiltered = results!!.values as MutableList<friend>
                notifyDataSetChanged()
            }
        }
    }

    var list: MutableList<friend>
    var listFiltered: MutableList<friend>
    var con: Context

    lateinit var rv: View

    constructor(list: MutableList<friend>, con: Context) : super(){
        this.list = list
        this.listFiltered = list
        this.con = con
    }


    companion object {
        class Holder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
            var tvName: TextView
            var btnNo: Button

            constructor(rv: View) : super(rv){
                tvName = rv.findViewById(R.id.tvName) as TextView
                btnNo = rv.findViewById(R.id.btnNo) as Button
            }
        }
    }


    override fun onBindViewHolder(p0: Holder, p1: Int) {
        var fr: friend
        fr = listFiltered.get(p1)
        p0.tvName.setText(fr.getFname())
        var id: Int = p1 + 1
        p0.btnNo.setText(id.toString())
        rv.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(con, p0.tvName.text.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        var holder: Holder
        rv = LayoutInflater.from(p0.context).inflate(R.layout.friend_row, p0, false)
        holder = Holder(rv)

        return holder
    }

}