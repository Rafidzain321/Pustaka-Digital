package com.example.pustaka.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pustaka.R
import com.example.pustaka.book_recycleview.Book
import com.google.firebase.database.FirebaseDatabase

class AdminBookAdapter(
    private var books: List<Book>,
    private val onDeleteClick: (String) -> Unit // Callback untuk hapus buku
) : RecyclerView.Adapter<AdminBookAdapter.AdminBookViewHolder>() {

    inner class AdminBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.bookImage)
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitle)
        val bookAuthor: TextView = itemView.findViewById(R.id.bookAuthor)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

        fun bind(book: Book) {
            bookTitle.text = book.title
            bookAuthor.text = "by ${book.author}"

            // Load gambar buku dengan Glide
            Glide.with(itemView.context)
                .load(book.coverimageUrl)
                .into(bookImage)

            // Event klik ikon tong sampah
            deleteIcon.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Hapus Buku")
                    .setMessage("Apakah Anda yakin ingin menghapus buku ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        onDeleteClick(book.id.toString()) // Callback untuk menghapus buku
                    }
                    .setNegativeButton("Tidak", null)
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_book_card, parent, false)
        return AdminBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminBookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
