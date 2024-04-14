package com.example.windealer

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.windealer.databinding.ActivityProfilePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.awaitAll

class ProfilePage : AppCompatActivity() {
    private lateinit var binding:ActivityProfilePageBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
auth = FirebaseAuth.getInstance()
        val dataref = FirebaseDatabase.getInstance().reference

        binding.logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are sure to logout?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,LoginPage::class.java))
                    finish()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .create()
                .show()

        }
        val profpic = intent.getStringExtra("profpic")
        Picasso.get().load(profpic.toString()).into(binding.profilepicture)
        binding.name.text = intent.getStringExtra("username")
        binding.address.text = intent.getStringExtra("useraddr")
        binding.mobileno.text = intent.getStringExtra("usercont")

        binding.profilebtn.setOnClickListener {
            val intent = Intent(this,EditProfile::class.java)
            intent.putExtra("Address",binding.address.text.toString())
            intent.putExtra("Mobileno.",binding.mobileno.text.toString())
            intent.putExtra("name",binding.name.text.toString())
            startActivity(intent)
            finish()
        }
        binding.addaccount.setOnClickListener {
            startActivity(Intent(this,SignUpPage::class.java))
            finish()
        }
    }
}