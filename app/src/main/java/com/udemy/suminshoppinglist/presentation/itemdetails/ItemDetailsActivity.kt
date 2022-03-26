package com.udemy.suminshoppinglist.presentation.itemdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udemy.suminshoppinglist.R
import com.udemy.suminshoppinglist.presentation.utils.PhoneOrientation

class ItemDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        /**
         * Это нужно для того, чтобы проверить пересоздавалось ли активити.
         * Если активити не пересоздавалось, метод будет вызван 1 раз.
         * (то есть мы не будем заново добавлять фрагмент, это сделает активити)
         * Если пересоздавалось 2 раза.
         */
        if (savedInstanceState == null) {
            launchRightMode()
        }
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