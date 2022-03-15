package com.udemy.suminshoppinglist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.databinding.ActivityMainBinding
import com.udemy.suminshoppinglist.presentation.itemdetails.ShopItemFragment
import com.udemy.suminshoppinglist.presentation.itemdetails.ItemDetailsActivity
import com.udemy.suminshoppinglist.presentation.main.adapter.ShopListAdapter
import com.udemy.suminshoppinglist.presentation.utils.PhoneOrientation
import com.udemy.suminshoppinglist.presentation.utils.UpdateList
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UpdateList {
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

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainerLand == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack() // чистит стек фрагментов в памяти
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container_land, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun settingButtonAdd() {
        binding.btnAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ItemDetailsActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem(PhoneOrientation.HORIZONTAL))
            }
        }
    }

    private fun showList(list: List<ShopItem>) {
        shopListAdapter.submitList(list) // внутри адаптера запускается новый поток
    }

    private fun settingsAdapter() {
        shopListAdapter = ShopListAdapter()
        binding.rcShopList.adapter = shopListAdapter
        binding.rcShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE)
        binding.rcShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE)

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
            if (isOnePaneMode()) {
                val intent =
                    ItemDetailsActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id, PhoneOrientation.HORIZONTAL))
            }
        }
    }

    private fun onLongClickListenerShopItem() {
        shopListAdapter.onShopLongClickListener = {
            mainViewModel.changeEnableState(it)
        }
    }

    // Для портретной ориентации
    override fun onResume() {
        super.onResume()
        mainViewModel.updateShopList()
    }

    // Для горизонтальной ориентации
    override fun update() {
        mainViewModel.updateShopList()
    }
}

/**
 * Метод inflate медленный и вызывается для каждого элемента.
 * Метод findViewById тоже медленный и вызывается для каждого
 * элемента списка несколько раз.
 * Даже если изменился всего один элемент, то нужно перерисовать весь список.
 * .let{} - позволяет делать что-то с объектом если он не null
 *
 * android:name="com.udemy.suminshoppinglist.presentation.itemdetails.ShopItemFragment"
 * позволяет сразу вставить фрагмент в макет
 * tools:layout="@layout/fragment_shop_item"
 * позволяет предварительно видеть макет
 * Фрагмент сначала прикрепляется к активити, потом создаётся view из макета.
 * Обращение к view в методе onCreate у фрагмента делать не нужно, только в onCreateView
 *
 * requireActivity() - вызывается у Активити к которому прикрёплён фрагмент.
 * Но! Фрагмент не может требовать чтобы активити была запущена каким-то определённым образом.
 * (требовать параметры)
 *
 * viewLifecycleOwner - нужно передавать вместо контекста к view, будет использоваться жизненный цикл
 * созданной view, если передать в LiveData - когда view умирает, LiveData отписывает его.
 *
 * activity и requireActivity() - отличия что activity вызывает нулабельный объект.
 *
 *  В конструкторе нельзя передавать значения, если при перевороте экрана\смены языка,
 *  система пересоздаст фрагмент, то будет вызван пустой конструктор, а если есть параметры -
 *  то приложение упадёт.
 *
 */