package com.example.windeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windeal.databinding.ActivityDealersReplyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DealersReply : AppCompatActivity() {
    private lateinit var binding:ActivityDealersReplyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealersReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.replyrv
        recyclerView.layoutManager = LinearLayoutManager(this)
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val notekey = intent.getStringExtra("notekey")

        currentUser?.let {
                user->
            Toast.makeText(this, notekey, Toast.LENGTH_SHORT).show()
            database.reference.child("users").child(user.uid).child("reply").child(notekey.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<RepliesData>()
                    for (snap in snapshot.children){
                        val value = snap.getValue(RepliesData::class.java)
                        value?.let {
                            list.add(it)
                        }
                    }
                    val adapter = RepliesAdapter(this@DealersReply,list,notekey.toString())
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}