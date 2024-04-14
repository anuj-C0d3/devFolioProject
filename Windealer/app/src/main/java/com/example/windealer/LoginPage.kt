package com.example.windealer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.windealer.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {
    private lateinit var binding:ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    override fun onStart() {
        auth = FirebaseAuth.getInstance()
        val cu = auth.currentUser
        if (cu!=null&&cu.isEmailVerified){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.Signup.setOnClickListener {
            startActivity(Intent(this,SignUpPage::class.java))
            finish()
        }
        auth = FirebaseAuth.getInstance()
        binding.email.setText(intent.getStringExtra("getEmail"))
        binding.password.setText(intent.getStringExtra("getPassword"))
        binding.loginBtn.setOnClickListener {
            val Emt = binding.email.text.toString()
            val pass = binding.password.text.toString()
            if(Emt.isEmpty()||pass.isEmpty()) {
                Toast.makeText(this, "Fill all fields.", Toast.LENGTH_SHORT).show()
            } else {

                auth.signInWithEmailAndPassword(Emt, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser!!.isEmailVerified){
                            val alertDialog = AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setIcon(R.drawable.editproficon)
                                .setTitle("Edit profile")
                                .setMessage("Do you want to edit your profile")
                                .setPositiveButton("Yes",
                                    DialogInterface.OnClickListener { dialog, which ->
                                    startActivity(Intent(this,EditProfile::class.java))
                                    finish()
                                })
                                .setNegativeButton("No",
                                    DialogInterface.OnClickListener { dialog, which ->
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()
                                })
                                .create()
                                .show()
                        } else {
                            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Login failed due to ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.forgetPassword.setOnClickListener {
            val email = binding.email.text.toString()
            if (email.isNotEmpty())  {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    Toast.makeText(this, "Email send", Toast.LENGTH_SHORT).show()
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Reset password")
                        .setNeutralButton("Sure!", DialogInterface.OnClickListener{ dialog, which ->
                            dialog.dismiss()
                        })
                    alertDialog.create()
                    alertDialog.show()
                }
            } else{
                Toast.makeText(this, "Please enter email.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}