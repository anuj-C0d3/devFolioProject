package com.example.windeal

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.windeal.databinding.ActivitySignUpPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import taimoor.sultani.sweetalert2.Sweetalert

class SignUpPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivitySignUpPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.Signupbtn.setOnClickListener {
            val email = binding.Email.text.toString()
            val mobile = binding.number.text.toString()
            val pass1 = binding.password1.text.toString()
            val pass2 = binding.password2.text.toString()
            if(email.isEmpty()||mobile.isEmpty()||pass1.isEmpty()||pass2.isEmpty()){
                Toast.makeText(this, "Fill all fields.", Toast.LENGTH_SHORT).show()
            } else if (pass1!=pass2){
                Toast.makeText(this, "Re enter password should be same.", Toast.LENGTH_SHORT).show()
            } else{
                auth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener { task->
                    if(task.isSuccessful) {
                        auth.currentUser!!.sendEmailVerification(ActionCodeSettings.zzb()).addOnCompleteListener {
                            task->
                            if (task.isSuccessful){
                                Sweetalert(this, Sweetalert.WARNING_TYPE)
                                    .setTitleText("Email verification")
                                    .setContentText ("Click on link send to given email $email")
                                    .showConfirmButton(true)
                                    .setConfirmButtonBackgroundColor("#002196F3")
                                    .setConfirmButtonTextColor("#2196F3")
                                    .setConfirmButton("Ok") { sDialog ->
                                        sDialog.dismissWithAnimation()
                                        val intent = Intent(this, LoginPage::class.java)
                                        intent.putExtra("getEmail", email)
                                        intent.putExtra("getPassword", pass1)
                                        startActivity(intent)
                                        finish()

                                    }
                                    .setCancelButtonBackgroundColor("#002196F3")
                                    .setCancelButtonTextColor("#FFF21818")
                                    .setCancelButton("Cancel") { sDialog ->
                                        sDialog.dismissWithAnimation()
                                    }

                                    .show()
                            } else {
                                Toast.makeText(this, "Verification Failed $it", Toast.LENGTH_SHORT).show()
                                Toast.makeText(this, "Enter valid email.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        }
    }

}