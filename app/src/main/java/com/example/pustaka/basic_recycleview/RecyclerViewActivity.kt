package com.example.pustaka.basic_recycleview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pustaka.R
import com.example.pustaka.welcome_screen.PagerAdapter

class RecyclerViewActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycler_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
       // recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        val items = listOf(
            ItemModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxPRLozVcJ_DTVxfd-qjDx5EfAM04GeddEp02tj7f0n6LDo-954kpH5FmexJJpgTMNOBY&usqp=CAU", "Description 1"),
            ItemModel("https://images.unsplash.com/photo-1532009324734-20a7a5813719?w=1024", "Description 2"),
            ItemModel("https://images.unsplash.com/photo-1524429656589-6633a470097c?w=1024", "Description 3"),
            ItemModel("https://images.unsplash.com/photo-1530224264768-7ff8c1789d79?w=1024", "Description 4"),
            ItemModel("https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=1024", "Description 5"),
            ItemModel("https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=1024", "Description 6"),
            ItemModel("https://images.unsplash.com/photo-1541698444083-023c97d3f4b6?w=1024", "Description 7"),
            ItemModel("https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=1024", "Description 8"),
            ItemModel("https://images.unsplash.com/photo-1542345812-d98b5cd6cf98?w=1024", "Description 9"),
            ItemModel("https://images.unsplash.com/photo-1471357674240-e1a485acb3e1?w=1024", "Description 10"),
            ItemModel("https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=1024", "Description 11"),
            ItemModel("https://images.unsplash.com/photo-1481277542470-605612bd2d61?w=1024", "Description 12"),
            ItemModel("https://images.unsplash.com/photo-1493612276216-ee3925520721?w=1024", "Description 13"),
            ItemModel("https://images.unsplash.com/photo-1481349518771-20055b2a7b24?w=1024", "Description 14"),
            ItemModel("https://images.unsplash.com/photo-1519985176271-adb1088fa94c?w=1024", "Description 15"),
            ItemModel("https://images.unsplash.com/photo-1495567720989-cebdbdd97913?w=1024", "Description 16"),
            ItemModel("https://images.unsplash.com/photo-1505751172876-fa1923c5c528?w=1024", "Description 17"),
            ItemModel("https://images.unsplash.com/photo-1503264116251-35a269479413?w=1024", "Description 18"),
            ItemModel("https://images.unsplash.com/photo-1503602642458-232111445657?w=1024", "Description 19"),
            ItemModel("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=1024", "Description 20"),
            ItemModel("https://images.unsplash.com/photo-1517202383675-eb0a6e27775f?w=1024", "Description 21"),
            ItemModel("https://images.unsplash.com/photo-1473923378535-13f8641f54a0?w=1024", "Description 22"),
            ItemModel("https://images.unsplash.com/photo-1431324000588-8ddd4cd52a06?w=1024", "Description 23"),
            ItemModel("https://images.unsplash.com/photo-1505678261036-a3fcc5e884ee?w=1024", "Description 24"),
            ItemModel("https://images.unsplash.com/photo-1500522144261-ea64433bbe27?w=1024", "Description 25"),
            ItemModel("https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=1024", "Description 26"),
            ItemModel("https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=1024", "Description 27"),
            ItemModel("https://images.unsplash.com/photo-1505245208761-ba872912fac0?w=1024", "Description 28"),
            ItemModel("https://images.unsplash.com/photo-1521747116042-5a810fda9664?w=1024", "Description 29"),
            ItemModel("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=1024", "Description 30")
        )

        adapter = ItemAdapter(items)
        recyclerView.adapter = adapter
    }
}