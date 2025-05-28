package com.example.pustaka.basic_api.data.model

data class FavoriteResponse(
    val id: Int,
    val bookId: Int,
    val createdAt: String,
    val updatedAt: String,
    val Book: BookFavorite
)

data class BookFavorite(
    val id: Int,
    val judul: String,
    val penulis: String,
    val kategori: String,
    val tanggal_terbit: String?,
    val synopsis: String,
    val file_pdf: String?,
    val cover: String?,
    val createdAt: String,
    val updatedAt: String
)

data class FavoriteRequest(
    val  bookId : Int
)