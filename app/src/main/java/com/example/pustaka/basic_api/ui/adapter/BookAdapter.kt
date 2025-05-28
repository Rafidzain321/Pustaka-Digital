package com.example.pustaka.basic_api.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.databinding.ListBookCardBinding
import com.squareup.picasso.Picasso

class BookAdapter(
    private var bookList: MutableList<Book>,
    private val onDeleteClickListener: (Book) -> Unit,
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBookCardBinding.inflate(
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

        holder.binding.apply {
            bookTitle.text = item.judul
            bookAuthor.text = item.kategori
            Picasso.get().load("http://103.217.145.203:3000/uploads/${item.cover}").into(bookImage)
        }

        holder.binding.deleteIcon.setOnClickListener {
            onDeleteClickListener(item)
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

    class ViewHolder(val binding: ListBookCardBinding) : RecyclerView.ViewHolder(binding.root)
}