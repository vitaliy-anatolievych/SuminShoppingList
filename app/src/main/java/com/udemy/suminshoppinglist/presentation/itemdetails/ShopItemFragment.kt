package com.udemy.suminshoppinglist.presentation.itemdetails

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udemy.domain.entities.ShopItem
import com.udemy.suminshoppinglist.app.App
import com.udemy.suminshoppinglist.databinding.FragmentShopItemBinding
import com.udemy.suminshoppinglist.presentation.utils.PhoneOrientation
import com.udemy.suminshoppinglist.presentation.utils.UpdateList
import com.udemy.suminshoppinglist.presentation.utils.ViewModelFactory
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {
    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw java.lang.RuntimeException("FragmentShopItemBinding == null")


    private lateinit var viewModel: ItemDetailsViewModel
    private var updateList: UpdateList? = null

    private var activityMode: String = UNDEFINED_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private lateinit var phoneOrientation: PhoneOrientation

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        activityMode = arguments?.getString(SCREEN_MODE) ?: UNDEFINED_MODE
        shopItemId = arguments?.getInt(ITEM_OBJECT_ID) ?: ShopItem.UNDEFINED_ID

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchRightMode()

        setInputErrorNameListener()
        setInputErrorCountListener()
        setShouldCloseScreenListener()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory)[ItemDetailsViewModel::class.java]
        launchPhoneOrientation()
    }

    private fun launchPhoneOrientation() {
        phoneOrientation = arguments?.getSerializable(LAND_SCAPE_MODE) as PhoneOrientation

        if (phoneOrientation == PhoneOrientation.HORIZONTAL) {
            if (activity is UpdateList) updateList = activity as UpdateList
            else throw java.lang.RuntimeException("Activity with ShopList must impl UpdateList")
        }
    }

    private fun launchRightMode() {
        when (activityMode) {
            MODE_EDIT -> {
                viewModel.getShopItem(itemId = shopItemId) {
                    activity?.runOnUiThread {
                        binding.tlName.editText?.setText(it.name)
                        binding.tlCount.editText?.setText(it.count.toString())

                        settingButtonSave()
                    }
                }
            }
            MODE_ADD -> {
                settingButtonSave()
            }
            else -> throw RuntimeException("Mode not fount $activityMode")
        }
    }

    private fun setInputErrorNameListener() {

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

//                    viewModel.addShopItem(name, count)
                    thread {
                        context?.contentResolver?.insert(
                            Uri.parse("content://com.udemy.suminshoppinglist/shop_items"),
                            ContentValues().apply {
                                put("id", 0)
                                put("name", name)
                                put("count", count.toInt())
                                put("enabled", true)
                            }
                        )
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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