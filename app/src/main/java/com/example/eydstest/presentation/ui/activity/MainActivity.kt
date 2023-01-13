package com.example.eydstest.presentation.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.eydstest.databinding.ActivityMainBinding
import com.example.eydstest.presentation.ui.adapter.GifPagerAdapter
import com.example.eydstest.presentation.util.Constant
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        val tabTitles = arrayOf(Constant.TAB_SEARCH, Constant.TAB_FAVORITE)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = GifPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}