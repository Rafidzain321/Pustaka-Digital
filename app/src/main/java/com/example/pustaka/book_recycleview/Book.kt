package com.example.pustaka.book_recycleview

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val year: Int,
    val synopsis: String,
    val coverimageUrl: String,
    val filePath: String
)