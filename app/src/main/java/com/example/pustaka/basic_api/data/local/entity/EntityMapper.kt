package com.example.pustaka.basic_api.data.local.entity

import com.example.pustaka.book_recycleview.Book

// Fungsi untuk mengonversi BookEntity ke Book
fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        category = category,
        year = year,
        synopsis = synopsis,
        coverimageUrl = coverImage, // Properti sesuai dengan coverImage
        filePath = filePath
    )
}

// Fungsi untuk mengonversi daftar BookEntity ke daftar Book
fun List<BookEntity>.toBooks(): List<Book> {
    return map { it.toBook() }
}
