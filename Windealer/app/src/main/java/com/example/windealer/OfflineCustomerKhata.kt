package com.example.windealer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windealer.databinding.ActivityOfflineCustomerKhataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OfflineCustomerKhata : AppCompatActivity() {
    private lateinit var binding:ActivityOfflineCustomerKhataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfflineCustomerKhataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.khataholder.text = intent.getStringExtra("name")
        binding.holderaddress.text = intent.getStringExtra("address")
        binding.totaldue.text = intent.getStringExtra("totaldue")
        var totaldue = binding.totaldue.text.toString().toInt()
        val rv = binding.emirv
        rv.layoutManager = LinearLayoutManager(this)
        val key = intent.getStringExtra("key")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val notkey = intent.getStringExtra("notkey")
        binding.emiamount.setText(intent.getStringExtra("emiamount"))
        binding.registorbutton.setOnClickListener {
            val emiamount = binding.emiamount.text.toString()
            val date = binding.dateofemi.text.toString()
            if (emiamount.isNotEmpty()&&date.isNotEmpty()){
                currentUser?.let {
                    val details = EmiRegistration(emiamount,date)
                    database.reference.child("customeremi").child(it.uid).child(key.toString()).push().setValue(details)
                        .addOnSuccessListener {
                             
                            totaldue=totaldue-emiamount.toInt()
                            if (totaldue<0){
                                Toast.makeText(this, "Now! You have to pay Rs.${totaldue} to customer ", Toast.LENGTH_SHORT).show()
                            }
                            binding.totaldue.text = totaldue.toString()
                            database.reference.child("dealercustomer").child(currentUser!!.uid).child("mycustomer").child(notkey.toString()).child("totaldue").setValue(totaldue.toString())
                        }
                }
                binding.registorbutton.isEnabled = false
            }else{
                Toast.makeText(this, "Fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }
        database.reference.child("customeremi").child(currentUser!!.uid).child(key.toString())
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val datalist = ArrayList<EmiRegistration>()
                    for (snap in snapshot.children){
                        val value= snap.getValue(EmiRegistration::class.java)
                        value?.let {
                            datalist.add(it)
                        }
                    }
                    val adapter = EmIDetailsAdapter(this@OfflineCustomerKhata,datalist)
                    rv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}