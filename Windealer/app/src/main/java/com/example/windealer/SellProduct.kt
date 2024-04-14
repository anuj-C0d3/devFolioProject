package com.example.windealer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.windealer.databinding.ActivitySellProductBinding
import kotlin.time.times

class SellProduct : AppCompatActivity() {
    private lateinit var binding:ActivitySellProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var price  = intent.getStringExtra("price")

      val priceint=  price?.toInt()
        binding.totalamount.isVisible = false
        binding.sellingProduct.text = intent.getStringExtra("product")
        binding.discount.isVisible = false
        binding.myFrameLayout.isVisible = false
        binding.SellingatEmi.isVisible = false
        binding.SellingatNoEmi.isVisible = false
        binding.gentbill.isVisible = false
        binding.proceed.setOnClickListener {
            var quantity = binding.sellingQuantity.text.toString()
            val name = binding.sellingName.text.toString()
            val address = binding.sellingAddress.text.toString()
            val mobileno = binding.sellingPhone.text.toString()
           if(name.isNotEmpty()&&address.isNotEmpty()&&mobileno.isNotEmpty()&&quantity.isNotEmpty())
           {
              var quantityint = quantity.toInt()

                val totalprice = priceint?.times(quantityint)
                binding.discount.isVisible = true
                binding.myFrameLayout.isVisible = true
                binding.SellingatEmi.isVisible = true
                binding.SellingatNoEmi.isVisible = true
                binding.gentbill.isVisible = true
                changeFragment(NoEmiFragment())
                binding.totalamount.text = "INR " + totalprice.toString() + "/-"
                binding.totalamount.isVisible = true
            }
            else {
               Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
           }
        }
        binding.SellingatNoEmi.setOnClickListener {
            changeFragment(NoEmiFragment())

        }
        binding.SellingatEmi.setOnClickListener {
            changeFragment(EmiFragment())

        }

    }
    private fun changeFragment(frag: Fragment) {
        val fragmentManager =supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.myFrameLayout,frag)
        fragmentManager.commit()
        fragmentManager.disallowAddToBackStack()
    }
}