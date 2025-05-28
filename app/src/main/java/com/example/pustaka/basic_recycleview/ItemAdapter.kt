package com.example.pustaka.basic_recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pustaka.R
import com.example.pustaka.databinding.HomeHorizontalItemBinding
import com.squareup.picasso.Picasso


class ItemAdapter (
    private val mList: List<ItemModel>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeHorizontalItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item = mList[position]

        Picasso.get().load(item.imageURL).into(holder.binding.newsHoriImage)
        holder.binding.newsHoriTitle.text = item.desc
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            Toast.makeText(context, "Anda klik item ke $position : ${item.desc}",Toast.LENGTH_SHORT).show()
            println("Anda Klik item ke  $position : ${item.desc}")
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(val binding: HomeHorizontalItemBinding): RecyclerView.ViewHolder(binding.root)
}