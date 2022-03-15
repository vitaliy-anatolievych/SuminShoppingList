package com.udemy.suminshoppinglist.presentation.itemdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.presentation.main.MainActivity
import com.udemy.suminshoppinglist.presentation.utils.PhoneOrientation
import com.udemy.suminshoppinglist.presentation.utils.UpdateList

class ItemDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        launchRightMode()
    }

    private fun launchRightMode() {
        val fragment = when (val activityMode = intent.getStringExtra(ACTIVITY_MODE)) {
            MODE_EDIT -> {
                val shopItemId = intent.getIntExtra(ITEM_OBJECT_ID, 0)
                ShopItemFragment.newInstanceEditItem(shopItemId, PhoneOrientation.VERTICAL)
            }
            MODE_ADD -> ShopItemFragment.newInstanceAddItem(PhoneOrientation.VERTICAL)
            else -> throw RuntimeException("Mode not fount $activityMode")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    companion object {
        private const val ITEM_OBJECT_ID = "itemObjectId"

        private const val ACTIVITY_MODE = "activityMode"
        private const val MODE_EDIT = "modeEdit"
        private const val MODE_ADD = "modeAdd"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, MODE_EDIT)
            intent.putExtra(ITEM_OBJECT_ID, id)
            return intent
        }
    }
}