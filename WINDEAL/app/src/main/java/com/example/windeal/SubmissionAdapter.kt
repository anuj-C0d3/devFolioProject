package com.example.windeal

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.windeal.databinding.ApplicationformfragBinding
import com.example.windeal.databinding.SubmisstionitemviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SubmissionAdapter(
    val context: Context,
    val list: ArrayList<UserData>,
    val onItemClickListener: OnItemClickListener
):RecyclerView.Adapter<SubmissionAdapter.viewHolder>() {
    interface OnItemClickListener{
        fun delete(notkey:String)
        fun update(notkey: String)
        fun open(notkey: String)
    }
    inner class viewHolder(val binding:SubmisstionitemviewBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val binding = SubmisstionitemviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.pgitem.text = list[position].producttype
        holder.binding.modelnumberitem.text = list[position].modelno

        holder.binding.delete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are sure to delete?")
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                    onItemClickListener.delete(list[position].notkey)
                })
                .setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })

                .create()
                .show()
        }

holder.binding.replies.setOnClickListener {
    context.startActivity(Intent(context,DealersReply::class.java).putExtra("notekey",list[position].notkey))
}
        holder.itemView.setOnClickListener {
            val itembinding = ApplicationformfragBinding.inflate(LayoutInflater.from(this.context))
            val alertDialog = AlertDialog.Builder(context).setView(itembinding.root)
            alertDialog.setTitle("Details")
                .setIcon(R.drawable.forms)
            itembinding.progressBar2.isVisible = false
            itembinding.enterText.isVisible = false
            itembinding.productGroup.isVisible = false

                itembinding.Name.setText(list[position].name)
                    itembinding.MobileNumber.setText(list[position].mobileNo)
            itembinding.PanNo.setText(list[position].idno)
            itembinding.ModelNo.setText(list[position].modelno)
            itembinding.city.setText(list[position].city)
            itembinding.dpay.setText(list[position].mindpay)
            itembinding.mEmi.setText(list[position].minemi)
            itembinding.contact.setText("Update")
            itembinding.contact.setOnClickListener {
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.let {
                val updateData = UserData(list[position].notkey,itembinding.Name.text.toString(),itembinding.MobileNumber.text.toString(),itembinding.PanNo.text.toString(),itembinding.city.text.toString(),list[position].producttype,itembinding.ModelNo.text.toString(),itembinding.dpay.text.toString(),itembinding.mEmi.text.toString(),it.uid,"false")
                    FirebaseDatabase.getInstance().reference.child("users").child(it.uid).child("requests").child(list[position].notkey).setValue(updateData).addOnSuccessListener {
                        Toast.makeText(context, "Successfully data updated.", Toast.LENGTH_SHORT).show()
                    }
                    FirebaseDatabase.getInstance().reference.child("userstodealer").child(itembinding.city.text.toString().replace("\\s".toRegex(), "").toLowerCase()).child(list[position].producttype.replace("\\s".toRegex(), "").toLowerCase())
                        .child(itembinding.ModelNo.text.toString().replace("\\s".toRegex(), "").toLowerCase()).child("data").child(list[position].notkey).setValue(updateData)
                }
            }

                alertDialog.create()
                    .show()

        }
    }
}