package com.example.windealer

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.windealer.databinding.OfflinecustomeritemviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Offline_cus_adapter(val context:Context,val list:ArrayList<OfflineCustomerDetails>):RecyclerView.Adapter<Offline_cus_adapter.viewHolder>() {
    inner class viewHolder(val binding:OfflinecustomeritemviewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Offline_cus_adapter.viewHolder {
        val binding = OfflinecustomeritemviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return viewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Offline_cus_adapter.viewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        holder.binding.productname.text = list[position].customer_name
        holder.binding.itemtype.text = list[position].product
        holder.binding.emipermonth.text = list[position].emi +"/Month"

        holder.binding.callcus.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+list[position].callno))
            context.startActivity(intent)

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context,OfflineCustomerKhata::class.java)
            intent.putExtra("name",list[position].customer_name)
            intent.putExtra("address",list[position].address)
            intent.putExtra("totaldue",list[position].totaldue)
            intent.putExtra("emiamount",list[position].emi)
            intent.putExtra("key",list[position].callno)
            intent.putExtra("notkey",list[position].notkey)
            context.startActivity(intent)
        }
       holder.itemView.setOnLongClickListener {
           holder.itemView.setBackgroundColor(Color.parseColor("#6B2196F3"))
           val alertDialog =AlertDialog.Builder(context)
               .setTitle("Remove")
               .setIcon(R.drawable.delete_icon)
               .setMessage("Are you sure to remove this customer?")
               .setNegativeButton("NO",DialogInterface.OnClickListener { dialog, which ->
                   dialog.dismiss()
               })
               .setPositiveButton("YES",DialogInterface.OnClickListener { dialog, which ->
                   currentUser?.let {
                       database.reference.child("dealercustomer").child(it.uid).child("mycustomer").child(list[position].notkey).removeValue()
                           database.reference.child("customeremi").child(it.uid).child(list[position].callno).removeValue()
                               .addOnCompleteListener {
                                   Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                               }
                            }
               })
               .create()
               .show()
           true
       }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}