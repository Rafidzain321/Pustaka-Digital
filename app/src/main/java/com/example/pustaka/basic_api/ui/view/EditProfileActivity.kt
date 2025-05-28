package com.example.pustaka.basic_api.ui.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pustaka.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var sharedPref: SharedPreferences
    private var imageUri: Uri? = null

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inisialisasi views
        profileImageView = findViewById(R.id.profileImageView)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        val uploadImageButton: Button = findViewById(R.id.uploadImageButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        // Setup SharedPreferences
        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Load data dari SharedPreferences
        loadProfileData()

        // Register launcher untuk memilih gambar
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                imageUri?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(profileImageView)
            }
        }

        // Tombol Upload Gambar
        uploadImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        // Tombol Save
        saveButton.setOnClickListener {
            saveProfileData()
        }
    }

    private fun loadProfileData() {
        val currentUsername = sharedPref.getString("username", "Guest")
        val currentGender = sharedPref.getString("gender", "")
        val currentImageUri = sharedPref.getString("profileImageUri", null)

        fullNameEditText.setText(currentUsername)

        if (currentGender == "Laki-laki") {
            genderRadioGroup.check(R.id.maleRadioButton)
        } else if (currentGender == "Perempuan") {
            genderRadioGroup.check(R.id.femaleRadioButton)
        }

        if (!currentImageUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(currentImageUri)
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.profile_picture)
                    .into(profileImageView)
            } catch (e: Exception) {
                profileImageView.setImageResource(R.drawable.profile_picture)
            }
        }
    }

    private fun saveProfileData() {
        val newUsername = fullNameEditText.text.toString().trim()
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        val newGender = when (selectedGenderId) {
            R.id.maleRadioButton -> "Laki-laki"
            R.id.femaleRadioButton -> "Perempuan"
            else -> ""
        }

        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Nama lengkap tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val editor = sharedPref.edit()
        editor.putString("username", newUsername)
        editor.putString("gender", newGender)
        editor.putString("profileImageUri", imageUri?.toString() ?: "")
        editor.apply()

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        pickImageLauncher.launch(intent)
    }
}
