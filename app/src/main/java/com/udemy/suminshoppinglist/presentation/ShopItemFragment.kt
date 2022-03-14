package com.udemy.suminshoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.databinding.FragmentShopItemBinding
import com.udemy.suminshoppinglist.presentation.itemdetails.ItemDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemFragment() : Fragment() {
    private lateinit var binding: FragmentShopItemBinding
    private val viewModel by viewModel<ItemDetailsViewModel>()

    private var activityMode: String = UNDEFINED_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    constructor(_activityMode: String) : this() {
        activityMode = _activityMode
    }

    constructor(_activityMode: String, _shopItemId: Int): this() {
        activityMode = _activityMode
        shopItemId = _shopItemId
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchRightMode()
        setInputErrorNameListener()
        setInputErrorCountListener()
        setShouldCloseScreenListener()
    }

    private fun launchRightMode() {
        when (activityMode) {
            MODE_EDIT -> {
                val shopItem = viewModel.getShopItem(itemId = shopItemId)

                binding.tlName.editText?.setText(shopItem.name)
                binding.tlCount.editText?.setText(shopItem.count.toString())

                settingButtonSave()
            }
            MODE_ADD -> {
                settingButtonSave()
            }
            else -> throw RuntimeException("Mode not fount $activityMode")
        }
    }

    private fun setInputErrorNameListener() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
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
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
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

    private fun settingButtonSave() {
        when (activityMode) {
            MODE_EDIT -> {
                binding.btnSave.setOnClickListener {
                    val name = binding.tlName.editText?.text.toString()
                    val count = binding.tlCount.editText?.text.toString()

                    if (shopItemId != ShopItem.UNDEFINED_ID) {
                        viewModel.editShopItem(name, count, shopItemId)
                    } else {
                        throw java.lang.RuntimeException("undefined id $shopItemId")
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
            else -> throw RuntimeException("Mode not fount $activityMode")
        }
    }

    private fun setShouldCloseScreenListener() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed() // нажимает кнопку назад
        }
    }

    companion object {
        private const val UNDEFINED_MODE = "undefinedMode"
        private const val MODE_EDIT = "modeEdit"
        private const val MODE_ADD = "modeAdd"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }
    }
}