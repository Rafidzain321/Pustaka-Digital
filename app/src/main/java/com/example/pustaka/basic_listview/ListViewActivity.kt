package com.example.pustaka.basic_listview

import android.os.Bundle
import android.widget.ListAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pustaka.R
import android.widget.ListView
import android.widget.Toast

class ListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView : ListView = findViewById(R.id.listview)

        val menuList = listOf(
            ListModel("Menu 1", "Deskripsi Menu 1", R.drawable.ic_menu),
            ListModel("Menu 2", "Deskripsi Menu 2", R.drawable.ic_home),
            ListModel("Menu 3", "Deskripsi Menu 3", R.drawable.ic_person),
            ListModel("Menu 4", "Deskripsi Menu 4", R.drawable.ic_profile),
            ListModel("Menu 5", "Deskripsi Menu 5", R.drawable.ic_settings),
        )

        val adapter = ListAdapter(this, menuList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = menuList[position]

            if(selectedItem.name == "Menu 5") {
                Toast.makeText(this, "Anda klik Menu 5",Toast.LENGTH_SHORT).show()
        }
        }
    }
}