package com.udemy.suminshoppinglist.presentation.itemdetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.databinding.FragmentShopItemBinding
import com.udemy.suminshoppinglist.presentation.utils.PhoneOrientation
import com.udemy.suminshoppinglist.presentation.utils.UpdateList
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemFragment : Fragment() {
    private lateinit var binding: FragmentShopItemBinding
    private val viewModel by viewModel<ItemDetailsViewModel>()
    private var updateList: UpdateList? = null

    private var activityMode: String = UNDEFINED_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private lateinit var phoneOrientation: PhoneOrientation


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShopItemBinding.inflate(inflater, container, false)

        activityMode = arguments?.getString(SCREEN_MODE) ?: UNDEFINED_MODE
        shopItemId = arguments?.getInt(ITEM_OBJECT_ID) ?: ShopItem.UNDEFINED_ID
        phoneOrientation = arguments?.getSerializable(LAND_SCAPE_MODE) as PhoneOrientation

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchPhoneOrientation()
        launchRightMode()

        setInputErrorNameListener()
        setInputErrorCountListener()
        setShouldCloseScreenListener()
    }

    private fun launchPhoneOrientation() {
        if (phoneOrientation == PhoneOrientation.HORIZONTAL) {
            if (activity is UpdateList) updateList = activity as UpdateList
            else throw java.lang.RuntimeException("Activity with ShopList must impl UpdateList")
        }
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
            updateList?.update()
        }
    }

    companion object {
        private const val UNDEFINED_MODE = "undefinedMode"
        private const val LAND_SCAPE_MODE = "landScapeMode"

        private const val SCREEN_MODE = "screenMode"
        private const val MODE_EDIT = "modeEdit"
        private const val MODE_ADD = "modeAdd"

        private const val ITEM_OBJECT_ID = "itemObjectId"

        fun newInstanceAddItem(landScape: PhoneOrientation): ShopItemFragment {
            val args = Bundle().apply {
                // сначала создастся Bundle,
                // а потом будут применены методы которые тут объявлены
                putString(SCREEN_MODE, MODE_ADD)
                putSerializable(LAND_SCAPE_MODE, landScape)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

        fun newInstanceEditItem(shopItemId: Int, landScape: PhoneOrientation): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(ITEM_OBJECT_ID, shopItemId)
                    putSerializable(LAND_SCAPE_MODE, landScape)
                }
            }
        }
    }
}