package com.example.windealer

data class ReplyData(val minimumdpay:String,val minimumemi:String,val message:String,val profilelink:String,val note:String){
    constructor() : this("","","","","")
}
