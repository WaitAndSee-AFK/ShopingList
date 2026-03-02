package com.example.shopinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopinglist.R
import com.example.shopinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class ShopItemFragment : Fragment() {

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var til_name: TextInputLayout
    private lateinit var tiet_name: TextInputEditText
    private lateinit var til_count: TextInputLayout
    private lateinit var tiet_count: TextInputEditText
    private lateinit var button_save: Button


    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID


    override fun onAttach(context: Context) {
        Log.d("ShopItemFragment", "onAttach")
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ShopItemFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observerViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.d("ShopItemFragment", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ShopItemFragment", "onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ShopItemFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ShopItemFragment", "onPause")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShopItemFragment", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ShopItemFragment", "onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("ShopItemFragment", "onDetach")
    }

    private fun observerViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            til_name.error = message
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            til_count.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }


    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }


    private fun addTextChangeListeners() {
        tiet_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }
        })

        tiet_count.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }
        })
    }


    private fun launchAddMode() {
        button_save.setOnClickListener {
            viewModel.addShopItem(
                tiet_name.text?.toString(),
                tiet_count.text?.toString()
            )
        }
    }


    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            tiet_name.setText(it.name)
            tiet_count.setText(it.count.toString())
        }
        button_save.setOnClickListener {
            viewModel.editShopItem(
                tiet_name.text?.toString(),
                tiet_count.text?.toString()
            )
        }
    }


    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknow screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }




    private fun initViews(view: View) {
        tiet_name = view.findViewById(R.id.tiet_name)
        tiet_count = view.findViewById(R.id.tiet_count)
        til_name = view.findViewById(R.id.til_name)
        til_count = view.findViewById(R.id.til_count)
        button_save = view.findViewById(R.id.button_save)
    }

    companion object {
        private const val SCREEN_MODE = "extra_mod"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mod_edit"
        private const val MODE_ADD = "mod_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem() : ShopItemFragment {
//            Поэтапное создание
//            val args = Bundle()
//            args.putString(SCREEN_MODE, MODE_ADD)
//            val fragment = ShopItemFragment()
//            fragment.arguments = args
//            return fragment

//            Создание в стиле kotlin
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int) : ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}