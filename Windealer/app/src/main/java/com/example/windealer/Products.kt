package com.example.windealer

data class Products(val producttype:String,val model:String,val city:String, val notkey:String,val price:String,val quantity:String){
    constructor() : this("","","","","","")
}
