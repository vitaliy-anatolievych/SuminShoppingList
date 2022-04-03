package com.udemy.suminshoppinglist.presentation.main.controllers

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// НЕ ИСПОЛЬЗУЕТСЯ
@Suppress("unused")
class MySwipeController: ItemTouchHelper.Callback() {

    private var swipeBack = false
    private var isHoldElement = false
    private var padding = 0

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

        when(direction) {
            ItemTouchHelper.LEFT -> {
                // тут может быть удаление элемента
            }
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return  0
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


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, isCurrentlyActive)

            if (dX <= 0) {
                if (isHoldElement) {
                    val view = recyclerView.findContainingItemView(viewHolder.itemView)

                    if (viewHolder.itemView.paddingRight <= 120) {
                        padding += 10
                        view?.setPadding(0, 0, padding, 0)
                    } else {
        //                        val imgButton = view?.findViewById<ImageButton>(R.id.image_button)
        //                        imgButton?.visibility = View.VISIBLE
                    }
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        recyclerView: RecyclerView, isCurrentlyActive: Boolean,
    ) {

        recyclerView.setOnTouchListener { view, motionEvent ->
            swipeBack = motionEvent.action == MotionEvent.ACTION_CANCEL || motionEvent.action == MotionEvent.ACTION_UP
            isHoldElement = isCurrentlyActive
            view.performClick()
            false
        }
        isHoldElement = isCurrentlyActive
    }
}



//            val itemView = viewHolder.itemView
//            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
//            val width = height / 3
//            val icon: Bitmap
//                p.color = Color.parseColor("#D32F2F")
//                val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(),itemView.right.toFloat(), itemView.bottom.toFloat())
//                c.drawRoundRect(background, 20f, 20f,  p)
//                icon = drawableToBitmap(recyclerView.context, R.drawable.ic_delete)
//                val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width,itemView.right.toFloat() - width,itemView.bottom.toFloat() - width)
//                c.drawBitmap(icon, null, icon_dest, p)

//    private fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
//        val drawable = AppCompatResources.getDrawable(context, drawableId)
//
//        if (drawable is BitmapDrawable) {
//            return drawable.bitmap
//        }
//
//        drawable?.let {
//            val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(bitmap)
//
//            drawable.setBounds(0, 0, canvas.width, canvas.height)
//            drawable.draw(canvas)
//            return bitmap
//        }
//        throw RuntimeException("drawableToBitmap: drawable is null")
//    }