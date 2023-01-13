package com.example.eydstest.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eydstest.presentation.ui.fragment.FavoriteGifFragment
import com.example.eydstest.presentation.ui.fragment.SearchGifFragment

class GifPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SearchGifFragment()
            else -> FavoriteGifFragment()
        }
    }
}