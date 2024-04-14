package com.example.windeal

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.windeal.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import taimoor.sultani.sweetalert2.Sweetalert

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
    @SuppressLint("SuspiciousIndentation")
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
                           Sweetalert(this, Sweetalert.WARNING_TYPE)
                               .setTitleText("Edit Profile")
                               .setContentText("Edi your profile...")
                               .showConfirmButton(true)
                               .setConfirmButtonBackgroundColor("#002196F3")
                               .setConfirmButtonTextColor("#2196F3")
                               .setConfirmButton(
                                   "Yes"
                               ) { sDialog ->
                                   sDialog.dismissWithAnimation()
                                   startActivity(Intent(this,EditProfile::class.java))
                                   finish()

                               }
                               .setCancelButtonBackgroundColor("#002196F3")
                               .setCancelButtonTextColor("#FFF21818")
                               .setCancelButton(
                                   "No"
                               ) { sDialog -> sDialog.dismissWithAnimation()
                                   startActivity(Intent(this,MainActivity::class.java))
                                   finish()}
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
                    .setNeutralButton("Sure!",DialogInterface.OnClickListener{dialog, which ->
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