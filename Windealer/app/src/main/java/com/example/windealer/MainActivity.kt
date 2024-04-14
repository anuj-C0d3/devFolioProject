package com.example.windealer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.windealer.databinding.ActivityMainBinding
import com.example.windealer.databinding.AddproductBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var city=""
        var producttype=""
        var model=""
        val database= FirebaseDatabase.getInstance()
        var profilename :String =""
        var adrress:String = ""
        var contactnum:String=""
        var profpic :String= ""

        binding.imgaddcust.setOnClickListener {
            startActivity(Intent(this, AddCustomer::class.java))
        }
        currentUser?.let {
            val imgref = Firebase.storage.reference.child("profilepic/" + it.uid)
            imgref.downloadUrl.addOnSuccessListener {

                Picasso.get().load(it.toString()).into(binding.profpic)
                profpic = it.toString()
                currentUser?.let  {
                    FirebaseDatabase.getInstance().reference.child("dealerprofiles").child(it.uid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snap in snapshot.children) {
                                    val value = snap.getValue(ProfileData::class.java)
                                    value?.let {
                                        profilename = it.username
                                        adrress = it.address
                                        contactnum = it.mobileno
                                    }

                                }
                            }
                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                }
            }
        }
        binding.profpic.setOnClickListener {
            val intent = Intent(this,ProfilePage::class.java)
            intent.putExtra("username",profilename)
            intent.putExtra("useraddr",adrress)
            intent.putExtra("profpic",profpic)
            intent.putExtra("usercont",contactnum)
            startActivity(intent)
            finish()
        }
        binding.onlineinqimg.setOnClickListener {
            startActivity(Intent(this, OnlineInquiries::class.java))
        }
        binding.addproductsimg.setOnClickListener {
            val binding = AddproductBinding.inflate(layoutInflater)
            val alertDialog = AlertDialog.Builder(this).setView(binding.root)
            binding.button.setOnClickListener {
                var producttype = binding.producttype.text.toString().toLowerCase()
                var model = binding.model.text.toString().toLowerCase()
                var city = binding.city.text.toString().toLowerCase()
                var price = binding.Priceamount.text.toString().toLowerCase()
                producttype = producttype.replace("\\s".toRegex(), "")
                model = model.replace("\\s".toRegex(), "")
                city = city.replace("\\s".toRegex(), "")

                if (producttype.isNotEmpty() && model.isNotEmpty() && city.isNotEmpty()) {
                    currentUser?.let {
                        val dataref =
                            FirebaseDatabase.getInstance().reference.child("dealerproducts")
                                .child(it.uid).child("details").push().key
                        val products = Products(producttype, model, city, dataref.toString(),price,"1")
                        FirebaseDatabase.getInstance().reference.child("dealerproducts")
                            .child(it.uid)
                            .child("details").child(dataref.toString()).setValue(products)
                            .addOnCompleteListener {
                                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show()
                                Toast.makeText(
                                    this,
                                    "Now you can access costomers of this product.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                    }
                } else {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.create()
            alertDialog.show()

        }
        binding.imgaddcust.setOnClickListener {
            startActivity(Intent(this, AddCustomer::class.java))
        }
        binding.yourproductsimg.setOnClickListener {
            startActivity(Intent(this, Dealers_Products::class.java))
        }
        binding.custdetailsimg.setOnClickListener {
            startActivity(Intent(this, CustomerManagment::class.java))
        }

        currentUser?.let { it ->
            FirebaseDatabase.getInstance().reference.child("dealerproducts").child(it.uid)
                .child("details").addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val datalist = ArrayList<UserData>()
                        for (snap in snapshot.children) {
                            val value = snap.getValue(Products::class.java)
                            value?.let {
                                city = it.city
                                producttype = it.producttype
                                model = it.model
                            }
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            FirebaseDatabase.getInstance().reference.child("userstodealer")
                                .child(city)
                                .child(producttype).child(model).child("data")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            val value = snap.getValue(UserData::class.java)
                                            value?.let {
                                                    currentUser?.let { user ->
                                                      val ref =  database.reference.child("myuserinquiryes")
                                                            .child(user.uid).child("inquiry").addListenerForSingleValueEvent(object :ValueEventListener{
                                                              override fun onDataChange(snapshot: DataSnapshot) {
                                                                  if (!snapshot.hasChild(value.notkey)){
                                                                      database.reference.child("myuserinquiryes").child(user.uid).child("inquiry").child(value.notkey).setValue(value)
                                                                  }
                                                              }

                                                              override fun onCancelled(error: DatabaseError) {
                                                                  TODO("Not yet implemented")
                                                              }

                                                          })


                                                }
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                })

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }

    }
}