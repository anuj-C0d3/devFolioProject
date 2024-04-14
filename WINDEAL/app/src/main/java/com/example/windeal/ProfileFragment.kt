package com.example.windeal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.windeal.databinding.ProfilefragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import taimoor.sultani.sweetalert2.Sweetalert


class ProfileFragment:Fragment(R.layout.profilefragment) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding = ProfilefragmentBinding.inflate(inflater,container,false)
        val dataref = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.logout.setOnClickListener {
            Sweetalert(context, Sweetalert.WARNING_TYPE)
                .setTitleText("Logout!!")
                .setContentText("Are sure to logout?")
                .showConfirmButton(true)
                .setConfirmButtonBackgroundColor("#002196F3")
                .setConfirmButtonTextColor("#2196F3")
                .setConfirmButton(
                    "Yes"
                ) { sDialog ->
                    sDialog.dismissWithAnimation()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(context,LoginPage::class.java))
                    activity?.finish()

                     }
                .setCancelButtonBackgroundColor("#002196F3")
                .setCancelButtonTextColor("#FFF21818")
                .setCancelButton(
                    "No"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()

        }

        currentUser?.let {
            val imgref = Firebase.storage.reference.child("profilepic/"+it.uid)
            imgref.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it.toString()).into(binding.profilepicture)
                val currentUser = FirebaseAuth.getInstance().currentUser
              currentUser?.let  {
                    FirebaseDatabase.getInstance().reference.child("userprofiles").child(it.uid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snap in snapshot.children) {
                                    val value = snap.getValue(ProfileData::class.java)
//                                    binding.progressBar6.isVisible = false
                                    binding.name.text = value?.username
                                    binding.address.text = value?.address
                                    binding.mobileno.text = value?.mobileno
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                }
            }
        }
        binding.profilebtn.setOnClickListener {
            val intent = Intent(context,EditProfile::class.java)
            intent.putExtra("Address",binding.address.text.toString())
            intent.putExtra("Mobileno.",binding.mobileno.text.toString())
            intent.putExtra("name",binding.name.text.toString())
            startActivity(intent)

        }
        binding.addaccount.setOnClickListener {
            startActivity(Intent(context,SignUpPage::class.java))
        }
        return binding.root
    }
}
