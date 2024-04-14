package com.example.windealer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windealer.databinding.EmidetailsBinding

class EmIDetailsAdapter(val context: Context,val list:ArrayList<EmiRegistration>):RecyclerView.Adapter<EmIDetailsAdapter.viewHolder>() {
   inner class viewHolder(val binding:EmidetailsBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmIDetailsAdapter.viewHolder {
        val binding = EmidetailsBinding.inflate(LayoutInflater.from(context),parent,false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmIDetailsAdapter.viewHolder, position: Int) {
        holder.binding.emiAmount.text = list[position].emiamount
        holder.binding.emidate.text = list[position].date
    }

    override fun getItemCount(): Int {
        return list.size
    }
}