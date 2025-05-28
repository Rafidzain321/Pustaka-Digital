package com.example.pustaka.basic_api.data.model

data class Book(
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

data class BookRrequest(
    val judul: String,
    val penulis: String,
    val kategori: String,
    val tanggalTerbit: String,
    val synopsis: String,
    val filePdf: String?,
    val cover: String?,
)