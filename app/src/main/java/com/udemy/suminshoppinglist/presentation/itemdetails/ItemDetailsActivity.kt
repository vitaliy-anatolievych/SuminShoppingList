package com.udemy.suminshoppinglist.presentation.itemdetails

import android.os.Bundle
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
        binding = ActivityItemDetailsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val activityMode = intent.getStringExtra(ACTIVITY_MODE)

        when(activityMode) {
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


    private fun settingButtonSave(mode: String) {


        when(mode) {
            MODE_EDIT -> {
                binding.btnSave.setOnClickListener {
                    val name = binding.tlName.editText?.text.toString()
                    val count = binding.tlCount.editText?.text.toString()

                    val id = intent.getIntExtra(ITEM_OBJECT_ID, ShopItem.UNDEFINED_ID)

                    if (id != ShopItem.UNDEFINED_ID) {
                        if (isCorrectFieldName(name) && isCorrectFieldCount(count)) {
                            viewModel.editShopItem(name, count.toInt(), id)
                            this.finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Some Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Не добавлен id элемета в .puExtra",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            MODE_ADD -> {
                binding.btnSave.setOnClickListener {
                    val name = binding.tlName.editText?.text.toString()
                    val count = binding.tlCount.editText?.text.toString()

                    if (isCorrectFieldName(name) && isCorrectFieldCount(count)) {
                        viewModel.addShopItem(name, count.toInt())
                        this.finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Some Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else -> throw RuntimeException("Mode not fount $mode")
        }
    }

    private fun isCorrectFieldName(name: String?): Boolean {
        var isCorrect = true

        if (name.isNullOrBlank() or name.isNullOrEmpty()) isCorrect = false
        return isCorrect
    }

    private fun isCorrectFieldCount(count: String?): Boolean {
        var isCorrect = true

        if (count.isNullOrBlank() or count.isNullOrEmpty()) isCorrect = false
        else {
            try {
                count?.toInt()
            } catch (e: Exception) {
                isCorrect = false
            }
        }

        return isCorrect
    }

    companion object {
        const val ITEM_OBJECT_NAME = "itemObjectName"
        const val ITEM_OBJECT_COUNT = "itemObjectCount"
        const val ITEM_OBJECT_ID = "itemObjectId"

        const val ACTIVITY_MODE = "activityMode"
        const val MODE_EDIT = "modeEdit"
        const val MODE_ADD = "modeAdd"
    }
}