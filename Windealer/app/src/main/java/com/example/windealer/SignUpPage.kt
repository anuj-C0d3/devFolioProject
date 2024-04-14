package com.example.windealer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.windealer.databinding.ActivitySignUpPageBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class SignUpPage : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpPageBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                                val alertDialog = AlertDialog.Builder(this)
                                    .setTitle("Email verification")
                                    .setMessage("Click on link send to given email $email")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                                        val intent = Intent(this, LoginPage::class.java)
                                        intent.putExtra("getEmail", email)
                                        intent.putExtra("getPassword", pass1)
                                        startActivity(intent)
                                        finish()

                                    })
                                    .setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, _ ->
                                        dialog.dismiss()
                                    })
                                    .create()
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