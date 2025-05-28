package com.example.pustaka.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pustaka.R
import com.example.pustaka.basic_api.ui.view.ManageBookFragment
import com.example.pustaka.databinding.ActivityAdminBinding
import com.example.pustaka.databinding.BottomSheetLayoutBinding
import com.example.pustaka.basic_api.ui.view.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale

class AdminActivity : AppCompatActivity() {
    private lateinit var toolbarText: TextView

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarText = findViewById(R.id.toolbarText)

        loadFragment(ManageBookFragment())

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(ManageBookFragment())
                    toolbarText.text = "Manage Book"
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    toolbarText.text = "Profile"
                    true
                }
                else -> false
            }
        }
    }

    private fun showAddBookBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        // Tanggal Terbit dengan DatePicker
        bottomSheetBinding.inputDate.setOnClickListener {
            showDatePicker(bottomSheetBinding)
        }

        // Simpan data ke Firebase
        bottomSheetBinding.saveButton.setOnClickListener {
            val title = bottomSheetBinding.inputTitle.text.toString().trim()
            val author = bottomSheetBinding.inputAuthor.text.toString().trim()
            val date = bottomSheetBinding.inputDate.text.toString().trim()
            val synopsis = bottomSheetBinding.inputSynopsis.text.toString().trim()

            if (title.isEmpty() || author.isEmpty() || date.isEmpty() || synopsis.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveBookToDatabase(title, author, date, synopsis)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showDatePicker(binding: BottomSheetLayoutBinding) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year)
                binding.inputDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveBookToDatabase(title: String, author: String, date: String, synopsis: String) {
        val database = FirebaseDatabase.getInstance().getReference("books")
        val bookId = database.push().key ?: return

        val book = mapOf(
            "id" to bookId,
            "title" to title,
            "author" to author,
            "date" to date,
            "synopsis" to synopsis
        )

        database.child(bookId).setValue(book)
            .addOnSuccessListener {
                Toast.makeText(this, "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menambahkan buku: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
