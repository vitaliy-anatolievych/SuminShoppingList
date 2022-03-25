package com.udemy.suminshoppinglist.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.databinding.ShopItemDisabledBinding
import com.udemy.suminshoppinglist.databinding.ShopItemEnabledBinding


class ShopListAdapter :
    ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopClickListener: ((ShopItem) -> Unit)? = null

    // Хранит элементы чтобы не вызывать много раз findViewById
    inner class ShopItemViewHolder(val _binding: ViewDataBinding) :
        RecyclerView.ViewHolder(_binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown ViewType $viewType")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
            layout,
            parent,
            false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)

        when (holder._binding) {
            is ShopItemEnabledBinding -> {
                holder._binding.tvName.text = shopItem.name
                holder._binding.tvCount.text = shopItem.count.toString()
            }
            is ShopItemDisabledBinding -> {
                holder._binding.tvName.text = shopItem.name
                holder._binding.tvCount.text = shopItem.count.toString()
            }
        }

        holder._binding.root.rootView.setOnClickListener {
            onShopClickListener?.invoke(shopItem)
        }

        holder._binding.root.rootView.setOnLongClickListener {
            onShopLongClickListener?.invoke(shopItem)
            true
        }
    }

    // нужен для того, чтобы использовать нужный макет в зависимости от типа элемента
    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)

        return if (shopItem.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 24
    }
}
/**
 * Без DifUtil Recycler перерисовывает все видимые элементы при изменении элемента.
 * @param notifyDataSetChanged() больше не рекомендуется к использованию.
 * Рекомендуется использовать более конкретные изменение которые произошли.
 * DifUtil сам вызывает необходимые методы при сравнении списков.
 *
 * В ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback())
 * вся работа со списком скрыва в ListAdapter
 */