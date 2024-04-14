package com.example.windealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windealer.databinding.ActivityCustomerManagmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomerManagment : AppCompatActivity() {
    private lateinit var binding:ActivityCustomerManagmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerManagmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rv = binding.offlinecusrv
        rv.layoutManager = LinearLayoutManager(this)
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            database.reference.child("dealercustomer").child(it.uid).child("mycustomer")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val datalist = ArrayList<OfflineCustomerDetails>()
                        for (snap in snapshot.children){
                            val value = snap.getValue(OfflineCustomerDetails::class.java)
                            value?.let {
                                datalist.add(it)
                            }
                        }
                        datalist.reverse()
                        val adapter= Offline_cus_adapter(this@CustomerManagment,datalist)
                        rv.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }
    }
}