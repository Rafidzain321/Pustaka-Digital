package com.example.pustaka.basic_api.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.ui.view.DetailBookActivity
import com.example.pustaka.basic_api.ui.view.ReadPdfActivity
import com.example.pustaka.databinding.ListBookCardBinding
import com.example.pustaka.databinding.ListBookItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class BookListAdapter(
    private var bookList: MutableList<Book>,
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun updateData(book: List<Book>) {
        bookList.clear()
        bookList.addAll(book)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookList[position]

        val originalDate = item.tanggal_terbit
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(originalDate)

        val formattedDate = if (date != null) {
            outputFormat.format(date)
        } else {
            originalDate
        }

        holder.binding.apply {
            bookTitle.text = item.judul
            bookAuthor.text = item.kategori
            Picasso.get().load("http://103.217.145.203:3000/uploads/${item.cover}").into(bookImage)
        }

        holder.binding.root.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailBookActivity::class.java).apply {
                putExtra("id_buku", item.id)
                putExtra("judul_buku", item.judul)
                putExtra("kategori", item.kategori)
                putExtra("penulis", item.penulis)
                putExtra("cover", "http://103.217.145.203:3000/uploads/${item.cover}")
                putExtra("tanggal_terbit", formattedDate)
                putExtra("synopsis", item.synopsis)
                putExtra("file_pdf", "http://103.217.145.203:3000/uploads/${item.file_pdf}")
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