package com.example.windeal


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment

import com.example.windeal.databinding.ApplicationformfragBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale


class ApplicationFrag:Fragment(R.layout.applicationformfrag) {
    private lateinit var binding: ApplicationformfragBinding
    private lateinit var spinner: Spinner
    var productGroup:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApplicationformfragBinding.inflate(inflater,container,false)
        spinner = binding.productGroup
        val spinnerItem = arrayListOf("Choose product Group","Smart phone","Computer","Electronics","Smart tv")
        if(spinner!=null){
            val adapter = ArrayAdapter(container!!.context,android.R.layout.simple_list_item_1,spinnerItem)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productGroup = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        var databaseRef :DatabaseReference
binding.progressBar2.isVisible = false
        val auth = FirebaseAuth.getInstance()

        binding.contact.setOnClickListener {

            val name = binding.Name.text
            databaseRef = FirebaseDatabase.getInstance().reference
            val mobileno = binding.MobileNumber.text
            val idno = binding.PanNo.text
            var city = binding.city.text.toString()
            var modelno = binding.ModelNo.text.toString()
            val mindwnpay = binding.dpay.text
            val emi = binding.mEmi.text
            val currentUser = auth.currentUser
var  notkey = ""

           if (name.isNotEmpty()&&mobileno.isNotEmpty()&&idno.isNotEmpty()&&city.isNotEmpty()&&modelno.isNotEmpty()&&mindwnpay.isNotEmpty()&&emi.isNotEmpty()&&productGroup!="Choose product Group") {
               binding.progressBar2.isVisible = true
               currentUser?.let {
                    notkey = databaseRef.child("users").child(it.uid).child(city.toLowerCase())
                       .child(productGroup.toLowerCase()).child(modelno.toLowerCase()).push().key.toString()
                    val userdata = UserData(
                        notkey,
                        name.toString(),
                        mobileno.toString(),
                        idno.toString(),
                        city,
                        productGroup,
                        modelno,
                        mindwnpay.toString(),
                        emi.toString(),
                        it.uid,"false"
                    )
                   databaseRef.child("userstodealer").child(city.replace("\\s".toRegex(), "").toLowerCase()).child(productGroup.replace("\\s".toRegex(), "").toLowerCase())
                        .child(modelno.replace("\\s".toRegex(), "").toLowerCase()).child("data").child(notkey).setValue(userdata)
                }
                currentUser?.let { user ->

                    val userdata = UserData(
                        notkey,
                        name.toString(),
                        mobileno.toString(),
                        idno.toString(),
                        city,
                        productGroup,
                        modelno,
                        mindwnpay.toString(),
                        emi.toString(),
                        user.uid,"false"
                    )
                    if (notkey != null) {

                        databaseRef.child("users").child(user.uid).child("requests").child(notkey)
                            .setValue(userdata)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    binding.progressBar2.isVisible = false
                                    Toast.makeText(context, "Successfully send", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }

                }
            }else{
               Toast.makeText(this.context, "Fill all fields", Toast.LENGTH_SHORT).show()
           }
        }
        return binding.root
    }


}