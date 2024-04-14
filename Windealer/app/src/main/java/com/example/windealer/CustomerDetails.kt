package com.example.windealer

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.PopupMenu.OnDismissListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.windealer.databinding.ActivityCustomerDetailsBinding
import com.example.windealer.databinding.ActivityOnlineInquiriesBinding
import com.example.windealer.databinding.ReplyformBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class CustomerDetails : AppCompatActivity() {
    private lateinit var binding:ActivityCustomerDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Name.text = intent.getStringExtra("username")
        binding.Modelno.text = "Model name: "+intent.getStringExtra("modelno")
        val accesskey = intent.getStringExtra("accesskey")
        val notekey = intent.getStringExtra("notekey")
        binding.idname.text = "Name: "+intent.getStringExtra("name")
        binding.panno.text = "Pan/Aadhar no.: "+intent.getStringExtra("panno")
        binding.place.text = "City: "+intent.getStringExtra("city")
        binding.itemtype.text = "Model name: "+intent.getStringExtra("modelname")
        binding.minidpay.text = "Maximum downpayment: "+intent.getStringExtra("maxdpay")
        binding.miniemi.text = "Maximum emi: "+intent.getStringExtra("maxemi")
        val imgref = Firebase.storage.reference.child("profilepic/" + accesskey)
        imgref.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it.toString()).into(binding.imageView2)
        }

        binding.reply.setOnClickListener {

            val binding = ReplyformBinding.inflate(layoutInflater)
            val alertDialog = AlertDialog.Builder(this).setView(binding.root)
            binding.send.setOnClickListener {
                       
                startActivity(Intent(this, OnlineInquiries::class.java))
                finish()
                val minimumDpay = binding.minimumdpay.text.toString()
                val minimumemi = binding.minimumEmi.text.toString()
                val message = binding.message.text.toString()
                val database =  FirebaseDatabase.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                FirebaseDatabase.getInstance().reference.child("myuserinquiryes").child(currentUser.toString()).child("inquiry").child(notekey.toString())
                    .child("status").setValue("true")
                val note = database.reference.child("Dealers").child(currentUser.toString()).child("myreply").push().key
                val reply = ReplyData(minimumDpay,minimumemi,message,currentUser.toString(),note.toString())

                 database.reference.child("users").child(accesskey.toString()).child("reply").child(notekey.toString()).push().setValue(reply)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show()
                    }
                database.reference.child("Dealers").child(currentUser.toString()).child("myreply").child(note.toString()).setValue(reply)

            }
            alertDialog.create()
            alertDialog.show()

        }


    }
}