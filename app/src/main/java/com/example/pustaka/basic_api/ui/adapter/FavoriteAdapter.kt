package com.example.pustaka.basic_api.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pustaka.basic_api.ui.view.FavoriteBookDetail
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.model.FavoriteResponse
import com.example.pustaka.databinding.ListBookItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteAdapter(
    private var bookList: MutableList<FavoriteResponse>,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun updateData(book: List<FavoriteResponse>) {
        bookList.clear()
        bookList.addAll(book)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookList[position]

        // Handle Date Formatting
        val originalDate = item.Book.tanggal_terbit
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        var formattedDate = originalDate
        try {
            val date = inputFormat.parse(originalDate)
            formattedDate = if (date != null) {
                outputFormat.format(date)
            } else {
                originalDate // Fallback if date parsing fails
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Bind the data to views
        holder.binding.apply {
            bookTitle.text = item.Book.judul
            bookAuthor.text = item.Book.kategori

            // Load image using Picasso with error handling
            Picasso.get()
                .load("http://103.217.145.203:3000/uploads/${item.Book.cover}")
                .into(bookImage)
        }

        // Set click listener to open detail page
        holder.binding.root.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, FavoriteBookDetail::class.java).apply {
                putExtra("id_buku", item.id)
                putExtra("judul_buku", item.Book.judul)
                putExtra("kategori", item.Book.kategori)
                putExtra("penulis", item.Book.penulis)
                putExtra("cover", "http://103.217.145.203:3000/uploads/${item.Book.cover}")
                putExtra("tanggal_terbit", formattedDate)
                putExtra("synopsis", item.Book.synopsis)
                putExtra("file_pdf", "http://103.217.145.203:3000/uploads/${item.Book.file_pdf}")
            }
            context.startActivity(intent)
        }
    }


    fun removeItem(book: Book) {
        val position = bookList.indexOfFirst { it.id == book.id }
        if (position != -1) {
            bookList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(val binding: ListBookItemBinding) : RecyclerView.ViewHolder(binding.root)
}