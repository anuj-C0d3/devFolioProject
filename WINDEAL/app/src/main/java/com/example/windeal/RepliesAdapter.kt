package com.example.windeal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.windeal.databinding.RepliesBinding
import com.example.windeal.databinding.ReplypopupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class RepliesAdapter(val context: Context, val datalist:ArrayList<RepliesData>,val notekey:String):RecyclerView.Adapter<RepliesAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:RepliesBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepliesBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
         return datalist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profilelink = datalist[position].profilelink
        val database = FirebaseDatabase.getInstance()
val currentUser = FirebaseAuth.getInstance().currentUser
        var phoneno=""
        database.reference.child("dealerprofiles").child(profilelink)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        val value = snap.getValue(ProfileData::class.java)
//                                    binding.progressBar6.isVisible = false
                        holder.binding.username.text = value?.username
                       phoneno= value?.mobileno.toString()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        val imgref = Firebase.storage.reference.child("profilepic/" + profilelink)
        imgref.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it.toString()).into(holder.binding.userprof)
        }
        holder.binding.dealermessage.text = datalist[position].message
        holder.binding.mdpay.text = "Minimum Downpayment:- "+datalist[position].minimumdpay
        holder.binding.memipay.text = "Minimum Emi:- "+datalist[position].minimumemi
        holder.binding.callicn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + phoneno)
            )
            context.startActivity(intent)
        }
    }
}