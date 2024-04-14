package com.example.windealer

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class myVIewMode:ViewModel() {

    var currentUser = FirebaseAuth.getInstance().currentUser
        var datalist = ArrayList<UserData>()
    fun onlineEnquiry(){
        currentUser?.let {
            FirebaseDatabase.getInstance().reference.child("myuserinquiryes").child(it.uid).child("inquiry").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (snap in snapshot.children){
                        val value = snap.getValue(UserData::class.java)
                        value?.let {
                            datalist.add(it)
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


    }
}