package com.example.windealer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windealer.databinding.ActivityOnlineInquiriesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class OnlineInquiries : AppCompatActivity() {
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var binding:ActivityOnlineInquiriesBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineInquiriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        var city = ""
        var producttype = ""
        var model=""
        val recyclerView = binding.rv
        val accesslist = ArrayList<ArrayList<String>>()

        recyclerView.layoutManager = LinearLayoutManager(this)
        val currentUser = auth.currentUser
        currentUser?.let {
            FirebaseDatabase.getInstance().reference.child("myuserinquiryes").child(it.uid).child("inquiry").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val datalist = ArrayList<UserData>()
                    for (snap in snapshot.children){
                        val value = snap.getValue(UserData::class.java)
                        value?.let {
                            datalist.add(it)
                        }

                    }
                    val adapter = InquiriesAdapter(this@OnlineInquiries,datalist)
                    val recyclerView = binding.rv
                    recyclerView.layoutManager = LinearLayoutManager(this@OnlineInquiries)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }


}