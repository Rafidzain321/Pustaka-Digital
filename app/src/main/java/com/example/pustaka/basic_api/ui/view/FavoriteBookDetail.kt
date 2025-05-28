package com.example.pustaka.basic_api.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pustaka.Home
import com.example.pustaka.R
import com.example.pustaka.basic_api.data.model.FavoriteRequest
import com.example.pustaka.basic_api.data.network.RetrofitInstance
import com.example.pustaka.basic_api.data.repository.BookRepository
import com.example.pustaka.basic_api.ui.viewModel.BookViewModel2
import com.example.pustaka.basic_api.utils.Resource
import com.example.pustaka.basic_api.utils.ViewModelFactory
import com.squareup.picasso.Picasso

class FavoriteBookDetail : AppCompatActivity() {
    private lateinit var bookViewModel: BookViewModel2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_book_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = BookRepository(RetrofitInstance.getPerpusApi())
        val viewModelFactory = ViewModelFactory(BookViewModel2::class.java) {
            BookViewModel2(repository)
        }
        bookViewModel = ViewModelProvider(this, viewModelFactory).get(BookViewModel2::class.java)

        val idBuku = intent.getIntExtra("id_buku", -1)
        val judulBuku = intent.getStringExtra("judul_buku")
        val kategori = intent.getStringExtra("kategori")
        val cover = intent.getStringExtra("cover")
        val tanggalTerbit = intent.getStringExtra("tanggal_terbit")
        val synopsis = intent.getStringExtra("synopsis")
        val filePdf = intent.getStringExtra("file_pdf")
        val penulis = intent.getStringExtra("penulis")

        val coverImageView = findViewById<ImageView>(R.id.imgBookCover)
        val titleTextView = findViewById<TextView>(R.id.txtBookTitle)
        val authorTextView = findViewById<TextView>(R.id.txtBookAuthor)
        val synopsisTextView = findViewById<TextView>(R.id.txtBookSynopsis)
        val releaseDate = findViewById<TextView>(R.id.txtBookRelease)
        val kategoriTextView = findViewById<TextView>(R.id.txtBookCategory)

        val favoriteButton = findViewById<Button>(R.id.btnRemoveFavorite)
        val readButton = findViewById<Button>(R.id.btnRead)

        titleTextView.text = judulBuku
        authorTextView.text = penulis
        releaseDate.text = tanggalTerbit
        kategoriTextView.text = kategori
        Picasso.get().load(cover).into(coverImageView)
        synopsisTextView.text = synopsis


        readButton.setOnClickListener {
            val intent = Intent(this, ReadPdfActivity::class.java).apply {
                putExtra("URL", filePdf)
            }
            startActivity(intent)
        }

        favoriteButton.setOnClickListener {
            bookViewModel.removeFromFavorites(this, idBuku)
        }

        bookViewModel.removeFavoriteStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    Toast.makeText(
                        this,
                        "Buku berhasil dihapus dari favorite!",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, Home::class.java))
                    finish()
                }

                is Resource.Error -> {
                }

                else -> {}
            }
        }
    }
}