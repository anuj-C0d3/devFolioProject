package com.example.windealer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.windealer.databinding.AddcustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCustomer : AppCompatActivity() {
    private lateinit var binding:AddcustomerBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddcustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()

        binding.send.setOnClickListener {
            val customername = binding.customerName.text.toString()
            val address = binding.customerAddress.text.toString()
            val idnum = binding.callnum.text.toString()
            val productprice = binding.price.text.toString()
            val downpay = binding.downPayment.text.toString()
            val emi = binding.emitext.text.toString()
            val datepurchase = binding.date.text.toString()
            val productname = binding.Product.text.toString()
            val totaldue = productprice.toInt() - downpay.toInt()
            val notkey = database.reference.child("dealercustomer").child(currentUser!!.uid).child("mycustomer")
                .push().key
            val offcusdetls = OfflineCustomerDetails(customername,address,idnum,productname,productprice,totaldue.toString(),downpay,emi,datepurchase,notkey.toString())
           if (customername.isNotEmpty()&&address.isNotEmpty()&&idnum.isNotEmpty()&&productprice.isNotEmpty()&&idnum.isNotEmpty()) {
                currentUser?.let {
                    database.reference.child("dealercustomer").child(it.uid).child("mycustomer")
                        .child(notkey.toString()).setValue(offcusdetls).addOnSuccessListener {
                            Toast.makeText(this, "New customer added", Toast.LENGTH_SHORT).show()
                            binding.send.isEnabled = false
                        }
                }
            }else
           {
               Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
           }

        }
    }
}