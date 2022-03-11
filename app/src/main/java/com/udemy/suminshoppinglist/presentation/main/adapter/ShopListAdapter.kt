package com.udemy.suminshoppinglist.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.R


class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>()  {

    var shopList = listOf<ShopItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    inner class ShopItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_item_enabled, parent, false)
        Log.d("ShopList", view.toString())
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]

        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }

    override fun getItemCount(): Int = shopList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    companion object {

        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0

        const val MAX_POOL_SIZE = 24
    }
}