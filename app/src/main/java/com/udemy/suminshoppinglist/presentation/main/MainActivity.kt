package com.udemy.suminshoppinglist.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.udemy.suminshoppinglist.databinding.ActivityMainBinding
import com.udemy.suminshoppinglist.presentation.main.adapter.ShopListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        adapterSettings()

        mainViewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }
    }

    private fun adapterSettings() {
        shopListAdapter = ShopListAdapter()
        binding.rcShopList.adapter = shopListAdapter
    }
}