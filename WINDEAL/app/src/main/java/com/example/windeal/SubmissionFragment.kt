package com.example.windeal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.createBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.windeal.databinding.ApplicationformfragBinding
import com.example.windeal.databinding.SubmissionsdataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubmissionFragment() : Fragment(R.layout.submissionsdata),SubmissionAdapter.OnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val auth = FirebaseAuth.getInstance()
        val currentuser = auth.currentUser
        val dataref = FirebaseDatabase.getInstance().reference
        val binding =SubmissionsdataBinding.inflate(inflater,container,false)
        val recyclerView = binding.submissionsrv
        recyclerView.layoutManager = LinearLayoutManager(context)

        currentuser?.let {user->
            dataref.child("users").child(user.uid).child("requests")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val datalist = ArrayList<UserData>()
                        for(snap in snapshot.children){
                            val value = snap.getValue(UserData::class.java)
                            value?.let {
                                datalist.add(it)
                            }
                        }

                        datalist.reverse()
                        val adapter = SubmissionAdapter(container!!.context,datalist,this@SubmissionFragment)
                        recyclerView.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

        return binding.root
    }

    override fun delete(notkey: String) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val dataref = FirebaseDatabase.getInstance().reference
        currentUser?.let { user->
            val rf = dataref.child("users").child(user.uid).child("requests").child(notkey)
                rf.removeValue()
                .addOnCompleteListener {
                    Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show()

                }
        }

    }

    override fun update(notkey: String) {

    }

    override fun open(notkey: String) {

    }


}