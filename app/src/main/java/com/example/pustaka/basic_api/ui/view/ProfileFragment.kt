package com.example.pustaka.basic_api.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pustaka.Login
import com.bumptech.glide.Glide
import com.example.pustaka.R
import com.example.pustaka.menu_fragment.ProfileListAdapter
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var usernameTextView: TextView
    private lateinit var profileImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi views
        usernameTextView = view.findViewById(R.id.usernameTextView)
        profileImage = view.findViewById(R.id.profileImage)

        // Menampilkan username dan foto profil yang diambil dari SharedPreferences
        updateProfileInfo()

        val profileOptions = listOf("Privacy Policy", "Logout")
        val listView = view.findViewById<ListView>(R.id.profileListView)

        val adapter = ProfileListAdapter(requireContext(), profileOptions)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kubuku.id/download/perpusdasleman/privasi.html"))
                startActivity(intent)
            } else if (position == 1) {
                showLogoutConfirmationDialog()
            }
        }

        // Button untuk Edit Profile
        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        // Refresh info profile setiap kali fragment ditampilkan
        updateProfileInfo()
    }

    private fun updateProfileInfo() {
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "Guest") ?: "Guest"
        val profileImageUri = sharedPref?.getString("profileImageUri", null)

        usernameTextView.text = username

        if (!profileImageUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(profileImageUri)
                Glide.with(this)
                    .load(uri)
                    .circleCrop() // Buat gambar jadi bulat
                    .placeholder(R.drawable.profile_picture)
                    .into(profileImage)
            } catch (e: Exception) {
                profileImage.setImageResource(R.drawable.profile_picture)

                val editor = sharedPref?.edit()
                editor?.remove("profileImageUri")
                editor?.apply()
            }
        } else {
            profileImage.setImageResource(R.drawable.profile_picture)
        }
    }


    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah Anda yakin ingin logout?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.clear()?.apply()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
                activity?.finish() // Menutup activity dan kembali ke login
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss() // Menutup dialog jika user memilih "Tidak"
            }

        builder.create().show()
    }
}
