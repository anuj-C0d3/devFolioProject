package com.example.windealer

class OfflineCustomerDetails(val customer_name:String,val address:String,val callno:String,val product:String, val price:String,val totaldue:String, val down_payment:String, val emi:String, val Purchase_date:String, val notkey:String ) {
    constructor() : this("","","","","","","","","","")
}