package com.example.windeal

import android.app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.drjacky.imagepicker.ImagePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.example.windeal.databinding.ActivityEditProfileBinding
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var  profile:ProfileData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
         profile = ProfileData()
        binding.progressBar4.isVisible = false

        binding.profileimage.setOnClickListener {
            ImagePicker.with(this)

                .provider(ImageProvider.BOTH)
                .cropSquare()
                .createIntentFromDialog { LauncherImage.launch(it) }
        }
        binding.profname.setText(intent.getStringExtra("name"))
        binding.profaddress.setText(intent.getStringExtra("Address"))
        binding.profnumber.setText(intent.getStringExtra("Mobileno."))
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.savebtn.setOnClickListener {
            val username = binding.profname.text.toString()
           val address = binding.profaddress.text.toString()
            val mobileno = binding.profnumber.text.toString()
            profile.username = username
            profile.address = address
            profile.mobileno = mobileno
           currentUser?.let {
               FirebaseDatabase.getInstance().reference.child("userprofiles").child(it.uid).child("user_data").setValue(profile)
                   .addOnCompleteListener {
                       startActivity(Intent(this,MainActivity::class.java))
                   }
           }
        }
    }
    val LauncherImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            if (it.data!= null){
                val currentUser = FirebaseAuth.getInstance().currentUser
                binding.progressBar4.isVisible = true
                currentUser?.let {user->

                    val ref = Firebase.storage.reference.child("profilepic/"+user.uid)
                    ref.putFile(it.data!!.data!!)
                        .addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                binding.progressBar4.isVisible = false
                                Picasso.get().load(it.toString()).into(binding.profileimage)
                            }
                        }
                }

            }
        }
    }
}


