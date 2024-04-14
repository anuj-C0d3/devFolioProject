package com.example.windeal

data class UserData(val notkey:String,val name:String,val mobileNo:String,val idno:String,val city:String,val producttype:String,val modelno:String, val mindpay:String,val minemi:String,val uid:String,val status:String){
    constructor() : this("","","","","","","","","","","")

}
