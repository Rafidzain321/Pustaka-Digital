package com.example.pustaka.menu_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pustaka.R

class ProfileListAdapter(
    context: Context,
    private val data: List<String> // List data yang akan ditampilkan
) : ArrayAdapter<String>(context, R.layout.profile_list_item, data) {

    // Override getView untuk menyesuaikan tampilan item ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Memastikan kita mendapatkan view untuk item list
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.profile_list_item, parent, false)

        // Menampilkan teks dari data pada TextView
        val profileOptionText: TextView = view.findViewById(R.id.profileOptionText)
        profileOptionText.text = data[position]

        return view
    }
}
