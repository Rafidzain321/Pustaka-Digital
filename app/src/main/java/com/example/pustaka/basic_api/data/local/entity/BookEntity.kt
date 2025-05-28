package com.example.pustaka.basic_api.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val year: Int,
    val coverImage: String,
    val synopsis: String,
    val filePath: String // Path PDF di penyimpanan lokal
)
