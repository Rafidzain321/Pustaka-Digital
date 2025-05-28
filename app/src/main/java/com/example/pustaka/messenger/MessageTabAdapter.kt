package com.example.pustaka.messenger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MessageTabAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Daftar fragment yang akan ditampilkan dalam setiap tab
    private val fragmentList = listOf(
        InformationFragment(),
    )

    // Mengembalikan jumlah fragment dalam daftar
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // Mengembalikan fragment berdasarkan posisi yang diminta
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
