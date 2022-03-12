package com.udemy.suminshoppinglist.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R


class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onShopLongClickListener: ( (ShopItem) -> Unit)? = null
    var onShopClickListener: ( (ShopItem) -> Unit)? = null

    // Хранит элементы чтобы не вызывать много раз findViewById
    inner class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView? = view.findViewById(R.id.tvName)
        val tvCount: TextView? = view.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown ViewType $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]

        holder.tvName?.text = shopItem.name
        holder.tvCount?.text = shopItem.count.toString()

        holder.view.setOnLongClickListener {
            onShopLongClickListener?.invoke(shopItem)
            true
        }

        holder.view.setOnClickListener {
            onShopClickListener?.invoke(shopItem)
        }
    }

    // Метод вызывается когда View будет переиспользоваться.
    // Можно установить значения по умолчанию.
//    override fun onViewRecycled(holder: ShopItemViewHolder) {
//        super.onViewRecycled(holder)
//    }


    // нужен для того, чтобы использовать нужный макет в зависимости от типа элемента
    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]

        return if (shopItem.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    override fun getItemCount(): Int = shopList.size

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 24
    }
}