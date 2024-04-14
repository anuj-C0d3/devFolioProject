package com.example.windealer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.windealer.databinding.DealerproductviewBinding
import com.example.windealer.databinding.SellProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class productsadapter(val context: Context,val list:ArrayList<Products>):RecyclerView.Adapter<productsadapter.viewHolder>(){
    inner class viewHolder(val binding:DealerproductviewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = DealerproductviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
return list.size    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        FirebaseDatabase.getInstance()
        val uid: String? = FirebaseAuth.getInstance().uid
        holder.binding.qntt.text= list[position].quantity
        holder.binding.amntprice.text = list[position].price
        holder.binding.productname.text = list[position].model
        holder.binding.itemtype.text = list[position].producttype
            var qntt = holder.binding.qntt.text.toString().toInt()
        holder.binding.addbtn.setOnClickListener {
//            qntt++
//            holder.binding.qntt.text = qntt.toString()
//            database.reference.child("dealerproducts").child(uid.toString()).child("details")
//                .child(list[position].notkey).child("quantity").setValue(qntt.toString())
        }
        holder.binding.dec.setOnClickListener {
//            qntt--
//            holder.binding.qntt.text = qntt.toString()
//            database.reference.child("dealerproducts").child(uid.toString()).child("details")
//                .child(list[position].notkey).child("quantity").setValue(qntt.toString()
           val intent = Intent(context,SellProduct::class.java)
            intent.putExtra("price",list[position].price)
            intent.putExtra("product",list[position].model)
            context.startActivity(intent)
        }
    }


}
