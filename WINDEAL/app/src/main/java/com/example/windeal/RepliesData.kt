package com.example.windeal

data class RepliesData(val minimumdpay:String,val minimumemi:String,val message:String,val profilelink:String,val note:String){
    constructor() : this("","","","","")
}