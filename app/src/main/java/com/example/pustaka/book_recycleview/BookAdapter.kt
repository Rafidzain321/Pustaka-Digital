package com.example.pustaka.book_recycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pustaka.R
import com.example.pustaka.book_recycleview.Book

class BookAdapter(
    private val context: Context,
    private var books: List<Book>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var filteredBooks: MutableList<Book> = books.toMutableList()
    private var toast: Toast? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_book_card, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = filteredBooks[position]
        holder.bind(book)

        // Handle klik pada item buku
        holder.itemView.setOnClickListener {
            toast?.cancel()
            toast = Toast.makeText(context, "Anda sedang menekan buku ${book.title}", Toast.LENGTH_SHORT)
            toast?.show()

            // Panggil callback dengan ID buku yang ditekan
            onItemClick(book.id)
        }
    }

    override fun getItemCount(): Int = filteredBooks.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        filteredBooks = books.toMutableList()
        notifyDataSetChanged()
    }

    fun filterBooks(query: String, category: String) {
        filteredBooks = if (query.isEmpty()) {
            books.filter { it.category.equals(category, ignoreCase = true) }.toMutableList()
        } else {
            books.filter {
                it.title.contains(query, ignoreCase = true) && it.category.equals(category, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookImage: ImageView = itemView.findViewById(R.id.bookImage)
        private val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        private val bookAuthor: TextView = itemView.findViewById(R.id.bookAuthor)

        fun bind(book: Book) {0
            bookTitle.text = book.title
            bookAuthor.text = book.author

            // Menggunakan Glide untuk memuat gambar
            Glide.with(context)
                .load(book.coverimageUrl)
                .into(bookImage)
        }
    }
}
