package com.example.blink._old

class friend {

    private lateinit var fname: String

    constructor(fname: String){
        this.setFname(fname)
    }

    fun getFname() : String{
        return fname
    }

    fun setFname(fname: String){
        this.fname = fname
    }
}