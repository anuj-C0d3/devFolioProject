package com.example.windeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.windeal.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
replaceFrameWithFragment(ApplicationFrag())
        val currentUser = auth.currentUser
        currentUser?.let {
            val imgref = Firebase.storage.reference.child("profilepic/" + it.uid)
            imgref.downloadUrl.addOnSuccessListener {
//                binding.progressBar3.isVisible = false
                Picasso.get().load(it.toString()).into(binding.profpic)
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.submission ->{
                    replaceFrameWithFragment(SubmissionFragment())
                    true
                }
               R.id.application->{
                   replaceFrameWithFragment(ApplicationFrag())
                   true
               }
                R.id.profile->{
                    replaceFrameWithFragment(ProfileFragment())
                    true
                }
                else->false
            }
        }
    }

      fun replaceFrameWithFragment(frag: Fragment) {
        val fragmanager = supportFragmentManager
        val transection = fragmanager.beginTransaction()
        transection.replace(R.id.framlayout,frag)
        transection.commit()
    }
}