package com.udemy.suminshoppinglist.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.databinding.ActivityMainBinding
import com.udemy.suminshoppinglist.presentation.main.adapter.ShopListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        
        mainViewModel.shopList.observe(this) {
            showList(it)
        }
    }

    private fun showList(list: List<ShopItem>) {
        binding.llShopList.removeAllViews()
        for (shopItem in list) {
            val layoutId = if (shopItem.enabled) {
                R.layout.shop_item_enabled
            } else {
                R.layout.shop_item_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, binding.llShopList, false)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvCount = view.findViewById<TextView>(R.id.tvCount)

            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()
            view.setOnLongClickListener {
                mainViewModel.changeEnableState(shopItem)
                true
            }
            binding.llShopList.addView(view)
        }
    }
}

/**
 * Метод inflate медленный и вызывается для каждого элемента.
 * Метод findViewById тоже медленный и вызывается для каждого
 * элемента списка несколько раз.
 * Даже если изменился всего один элемент, то нужно перерисовать весь список.
 */