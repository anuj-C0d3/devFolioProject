package com.example.windealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windealer.databinding.ActivityDealersProductsBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Dealers_Products : AppCompatActivity() {
    private lateinit var binding:ActivityDealersProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealersProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(this)
        val database =  FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            database.reference.child("dealerproducts").child(it.uid).child("details").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val datalist = ArrayList<Products>()
                    for (snap in snapshot.children){
                        val value = snap.getValue(Products::class.java)
                        value?.let {
                            datalist.add(it)
                        }
                    }
                    val adapter = productsadapter(this@Dealers_Products,datalist)
                    rv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}