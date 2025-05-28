package com.example.pustaka

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pustaka.book_recycleview.Book
import com.example.pustaka.databinding.ActivityAddBookBinding
import com.google.firebase.database.FirebaseDatabase

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating layout dengan View Binding
        setContentView(R.layout.activity_add_book)
        // Setup tombol simpan
        binding.btnSave.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {
        // Ambil input dari user
        val title = binding.inputTitle.text.toString()
        val author = binding.inputAuthor.text.toString()
        val category = binding.inputCategory.text.toString()
        val year = binding.inputYear.text.toString()
        val synopsis = binding.inputSynopsis.text.toString()

        // Validasi input
        if (title.isEmpty() || author.isEmpty() || category.isEmpty() || year.isEmpty() || synopsis.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        // Tambahkan data ke Firebase Realtime Database
        val bookId = FirebaseDatabase.getInstance().getReference("Books").push().key!!
        val book = Book(
            id = bookId.hashCode(), // Buat ID unik
            title = title,
            author = author,
            category = category,
            year = year.toInt(),
            synopsis = synopsis,
            coverimageUrl = "coverImage", // Placeholder image
            filePath = "" // Kosongkan untuk sekarang
        )

        FirebaseDatabase.getInstance().getReference("Books")
            .child(bookId)
            .setValue(book)
            .addOnSuccessListener {
                Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman sebelumnya
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to add book: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
