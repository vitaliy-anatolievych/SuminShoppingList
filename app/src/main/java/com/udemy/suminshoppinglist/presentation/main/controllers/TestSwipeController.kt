package com.udemy.suminshoppinglist.presentation.main.controllers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.udemy.suminshoppinglist.presentation.main.MainViewModel
import com.udemy.suminshoppinglist.presentation.main.adapter.ShopListAdapter

// НЕ ИСПОЛЬЗУЕТСЯ
@Suppress("unused")
class TestSwipeController(private val viewModel: MainViewModel, private val  shopListAdapter: ShopListAdapter) : ItemTouchHelper.Callback() {
    private var swipeBack = false
    private var isAlertDialogShowed = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {

        setTouchListener(recyclerView)
        Log.d("ShowDx", "$dX")
        if (dX > -300f && !isAlertDialogShowed) {

                isAlertDialogShowed = true

                val alertDialog = AlertDialog.Builder(recyclerView.context)
                    .setTitle("Title")
                    .setMessage("Удалить?")
                    .setCancelable(false)
                    .setPositiveButton("Да") { _, _ ->
                        val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                        viewModel.deleteItem(item)
                        isAlertDialogShowed = false
                    }
                    .setNegativeButton("Нет") { _, _ ->
                        isAlertDialogShowed = false
                    }

                alertDialog.create().show()

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        recyclerView: RecyclerView,
    ) {
        recyclerView.setOnTouchListener { view, motionEvent ->
            swipeBack =
                motionEvent.action == MotionEvent.ACTION_CANCEL || motionEvent.action == MotionEvent.ACTION_UP
            view.performClick()
            false
        }
    }
}