package com.udemy.suminshoppinglist.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.databinding.ActivityMainBinding
import com.udemy.suminshoppinglist.presentation.itemdetails.ItemDetailsActivity
import com.udemy.suminshoppinglist.presentation.main.adapter.ShopListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        settingsAdapter()
        settingButtonAdd()

        mainViewModel.shopList.observe(this) {
            showList(it)
        }
    }

    private fun settingButtonAdd() {
        binding.btnAddShopItem.setOnClickListener {
            val intent = ItemDetailsActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun showList(list: List<ShopItem>) {
        shopListAdapter.submitList(list) // внутри адаптера запускается новый поток
    }

    private fun settingsAdapter() {
        shopListAdapter = ShopListAdapter()
        binding.rcShopList.adapter = shopListAdapter
        binding.rcShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
        binding.rcShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_DISABLED, ShopListAdapter.MAX_POOL_SIZE)

        onLongClickListenerShopItem()
        onTouchClickListenerShopItem()
        onSwipeListenerShopItem()
    }

    private fun onSwipeListenerShopItem() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                mainViewModel.deleteItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rcShopList)
    }

    private fun onTouchClickListenerShopItem() {
        shopListAdapter.onShopClickListener = {
            val intent = ItemDetailsActivity.newIntentEditItem(this, it.name, it.count.toString(), it.id)
            startActivity(intent)
        }
    }

    private fun onLongClickListenerShopItem() {
        shopListAdapter.onShopLongClickListener = {
            mainViewModel.changeEnableState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.updateShopList()
    }
}

/**
 * Метод inflate медленный и вызывается для каждого элемента.
 * Метод findViewById тоже медленный и вызывается для каждого
 * элемента списка несколько раз.
 * Даже если изменился всего один элемент, то нужно перерисовать весь список.
 * .let{} - позволяет делать что-то с объектом если он не null
 */