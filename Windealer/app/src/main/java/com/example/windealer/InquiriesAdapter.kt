package com.example.windealer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.windealer.databinding.InquiriesviewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.google.firebase.storage.storageMetadata
import com.squareup.picasso.Picasso
import kotlinx.coroutines.selects.whileSelect
import java.sql.Struct

class InquiriesAdapter(val context: Context, val datalist:ArrayList<UserData>):RecyclerView.Adapter<InquiriesAdapter.viewHolder>() {
    inner class viewHolder( val binding:InquiriesviewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
       val  binding = InquiriesviewBinding.inflate(LayoutInflater.from(context),parent,false)
       return viewHolder(binding)
    }

    override fun getItemCount(): Int {
       return datalist.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        val accesskey = datalist[position].uid
        val currentUser = FirebaseAuth.getInstance().currentUser
         FirebaseDatabase.getInstance().reference.child("userprofiles").child(accesskey).addValueEventListener(
             object :ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     for (snap in snapshot.children){
                         val value = snap.getValue(ProfileData::class.java)
                         if (value != null) {
                             holder.binding.productname.text = value.username
                             holder.binding.itemtype.text = datalist[position].modelno
                         }
                     }
                 }
                 override fun onCancelled(error: DatabaseError) {

                 }
             }
         )
        var stts:Boolean= false

if (!datalist[position].status.toBoolean()){
    holder.binding.statussign.text = "Not replied"
    holder.binding.statussign.setTextColor(Color.parseColor("#FF1100"))

}else {
    holder.binding.statussign.text = "Replied"
    holder.binding.statussign.setTextColor(Color.parseColor("#2F89FC"))
}
            val imgref = Firebase.storage.reference.child("profilepic/" + accesskey)
            imgref.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it.toString()).into(holder.binding.userprof)
            }
holder.itemView.setOnClickListener {
       val intent = Intent(context,CustomerDetails::class.java)
    intent.putExtra("username",holder.binding.productname.text.toString())
    intent.putExtra("modelno",holder.binding.itemtype.text.toString())
    intent.putExtra("accesskey",accesskey)
    intent.putExtra("name",datalist[position].name)
    intent.putExtra("panno",datalist[position].idno)
    intent.putExtra("city",datalist[position].city)
    intent.putExtra("modelname",datalist[position].modelno)
    intent.putExtra("maxdpay",datalist[position].mindpay)
    intent.putExtra("maxemi",datalist[position].minemi)
    intent.putExtra("notekey",datalist[position].notkey)
    intent.putExtra("producttype",datalist[position].producttype)
    context.startActivity(intent)
}
    }
}