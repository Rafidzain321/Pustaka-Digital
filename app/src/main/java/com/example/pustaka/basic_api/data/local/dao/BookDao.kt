package com.example.pustaka.basic_api.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.pustaka.basic_api.data.local.entity.BookEntity

@Dao
interface BookDao {

    // Fungsi untuk mengambil semua buku (tanpa LiveData)
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity> // Pastikan ini mengembalikan List langsung

    // Fungsi untuk mendapatkan buku berdasarkan ID
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookEntity?

    // Fungsi untuk menambah buku
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    // Fungsi untuk menghapus buku
    @Delete
    suspend fun delete(book: BookEntity)

    // Fungsi untuk mencari buku berdasarkan query
    @Query("SELECT * FROM books WHERE title LIKE :query")
    suspend fun searchBooks(query: String): List<BookEntity>
}
