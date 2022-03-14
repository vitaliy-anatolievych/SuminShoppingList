package com.udemy.suminshoppinglist.presentation.itemdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.databinding.ActivityItemDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class ItemDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailsBinding
    private val viewModel by viewModel<ItemDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityItemDetailsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        launchRightMode()

        setInputErrorNameListener()
        setInputErrorCountListener()
        setShouldCloseScreenListener()
    }

    private fun launchRightMode() {
        when (val activityMode = intent.getStringExtra(ACTIVITY_MODE)) {
            MODE_EDIT -> {
                val name = intent.getStringExtra(ITEM_OBJECT_NAME)
                val count = intent.getStringExtra(ITEM_OBJECT_COUNT)

                binding.tlName.editText?.setText(name)
                binding.tlCount.editText?.setText(count)

                settingButtonSave(mode = activityMode)
            }
            MODE_ADD -> {
                settingButtonSave(mode = activityMode)
            }
            else -> throw RuntimeException("Mode not fount $activityMode")
        }
    }

    private fun setInputErrorNameListener() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                "Ошибка ввода"
            } else {
                null
            }
            binding.tlName.error = message
        }

        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tlName.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setInputErrorCountListener() {
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                "Ошибка ввода"
            } else {
                null
            }
            binding.tlCount.error = message
        }

        binding.edCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tlCount.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun settingButtonSave(mode: String) {
        when (mode) {
            MODE_EDIT -> {
                binding.btnSave.setOnClickListener {
                    val name = binding.tlName.editText?.text.toString()
                    val count = binding.tlCount.editText?.text.toString()

                    val id = intent.getIntExtra(ITEM_OBJECT_ID, ShopItem.UNDEFINED_ID)

                    if (id != ShopItem.UNDEFINED_ID) {
                        viewModel.editShopItem(name, count, id)
                    } else {
                        throw java.lang.RuntimeException("undefined id $id")
                    }
                }
            }
            MODE_ADD -> {
                binding.btnSave.setOnClickListener {
                    val name = binding.tlName.editText?.text.toString()
                    val count = binding.tlCount.editText?.text.toString()

                    viewModel.addShopItem(name, count)
                }
            }
            else -> throw RuntimeException("Mode not fount $mode")
        }
    }

    private fun setShouldCloseScreenListener() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    companion object {
        private const val ITEM_OBJECT_NAME = "itemObjectName"
        private const val ITEM_OBJECT_COUNT = "itemObjectCount"
        private const val ITEM_OBJECT_ID = "itemObjectId"

        private const val ACTIVITY_MODE = "activityMode"
        private const val MODE_EDIT = "modeEdit"
        private const val MODE_ADD = "modeAdd"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, name: String?, count: String?, id: Int): Intent {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, MODE_EDIT)
            intent.putExtra(ITEM_OBJECT_NAME, name)
            intent.putExtra(ITEM_OBJECT_COUNT, count)
            intent.putExtra(ITEM_OBJECT_ID, id)
            return intent
        }
    }
}