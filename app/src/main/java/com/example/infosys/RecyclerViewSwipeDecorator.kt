package com.example.infosys

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSwipeDecorator private constructor() {
    private var context: Context? = null
    private var canvas: Canvas? = null
    private var recyclerView: RecyclerView? = null
    private var viewHolder: RecyclerView.ViewHolder? = null
    private var dX = 0f
    private var dY = 0f
    private var actionState = 0
    private var isCurrentlyActive = false
    private var swipeLeftBackgroundColor = 0
    private var swipeLeftActionIconId = 0
    private var swipeRightBackgroundColor = 0
    private var swipeRightActionIconId = 0
    private var iconHorizontalMargin = 0

    /**
     * Create a @RecyclerViewSwipeDecorator
     * @param context A valid Context object for the RecyclerView
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     */
    constructor(context: Context, canvas: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) : this() {
        this.context = context
        this.canvas = canvas
        this.recyclerView = recyclerView
        this.viewHolder = viewHolder
        this.dX = dX
        this.dY = dY
        this.actionState = actionState
        this.isCurrentlyActive = isCurrentlyActive
        iconHorizontalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt()
    }

    /**
     * Set the background color for either (left/right) swipe directions
     * @param backgroundColor The resource id of the background color to be set
     */
    fun setBackgroundColor(backgroundColor: Int) {
        swipeLeftBackgroundColor = backgroundColor
        swipeRightBackgroundColor = backgroundColor
    }

    /**
     * Set the action icon for either (left/right) swipe directions
     * @param actionIconId The resource id of the icon to be set
     */
    fun setActionIconId(actionIconId: Int) {
        swipeLeftActionIconId = actionIconId
        swipeRightActionIconId = actionIconId
    }

    /**
     * Set the background color for left swipe direction
     * @param swipeLeftBackgroundColor The resource id of the background color to be set
     */
    fun setSwipeLeftBackgroundColor(swipeLeftBackgroundColor: Int) {
        this.swipeLeftBackgroundColor = swipeLeftBackgroundColor
    }

    /**
     * Set the action icon for left swipe direction
     * @param swipeLeftActionIconId The resource id of the icon to be set
     */
    fun setSwipeLeftActionIconId(swipeLeftActionIconId: Int) {
        this.swipeLeftActionIconId = swipeLeftActionIconId
    }

    /**
     * Set the background color for right swipe direction
     * @param swipeRightBackgroundColor The resource id of the background color to be set
     */
    fun setSwipeRightBackgroundColor(swipeRightBackgroundColor: Int) {
        this.swipeRightBackgroundColor = swipeRightBackgroundColor
    }

    /**
     * Set the action icon for right swipe direction
     * @param swipeRightActionIconId The resource id of the icon to be set
     */
    fun setSwipeRightActionIconId(swipeRightActionIconId: Int) {
        this.swipeRightActionIconId = swipeRightActionIconId
    }

    /**
     * Set the horizontal margin of the icon (default is 16dp)
     * @param iconHorizontalMargin the margin in pixels
     */
    fun setIconHorizontalMargin(iconHorizontalMargin: Int) {
        this.iconHorizontalMargin = iconHorizontalMargin
    }

    /**
     * Decorate the RecyclerView item with the chosen backgrounds and icons
     */
    fun decorate() {
        try {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return
            if (dX > 0) { // Swiping Right
                if (swipeRightBackgroundColor != 0) {
                    val background = ColorDrawable(swipeRightBackgroundColor)
                    background.setBounds(0, viewHolder!!.itemView.top, viewHolder!!.itemView.left + dX.toInt(), viewHolder!!.itemView.bottom)
                    background.draw(canvas!!)
                }
                if (swipeRightActionIconId != 0) {
                    val icon = ContextCompat.getDrawable(context!!, swipeRightActionIconId)
                    val halfIcon = icon!!.intrinsicHeight / 2
                    val top = viewHolder!!.itemView.top + ((viewHolder!!.itemView.bottom - viewHolder!!.itemView.top) / 2 - halfIcon)
                    icon.setBounds(iconHorizontalMargin, top, iconHorizontalMargin + icon.intrinsicWidth, top + icon.intrinsicHeight)
                    icon.draw(canvas!!)
                }
            } else if (dX < 0) { // Swiping Left
                if (swipeLeftBackgroundColor != 0) {
                    val background = ColorDrawable(swipeLeftBackgroundColor)
                    background.setBounds(viewHolder!!.itemView.right + dX.toInt(), viewHolder!!.itemView.top, viewHolder!!.itemView.right, viewHolder!!.itemView.bottom)
                    background.draw(canvas!!)
                }
                if (swipeLeftActionIconId != 0) {
                    val icon = ContextCompat.getDrawable(context!!, swipeLeftActionIconId)
                    val halfIcon = icon!!.intrinsicHeight / 2
                    val top = viewHolder!!.itemView.top + ((viewHolder!!.itemView.bottom - viewHolder!!.itemView.top) / 2 - halfIcon)
                    icon.setBounds(viewHolder!!.itemView.right - iconHorizontalMargin - halfIcon * 2, top, viewHolder!!.itemView.right - iconHorizontalMargin, top + icon.intrinsicHeight)
                    icon.draw(canvas!!)
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * A Builder for the RecyclerViewSwipeDecorator class
     */
    class Builder(context: Context, canvas: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        private val mDecorator: RecyclerViewSwipeDecorator = RecyclerViewSwipeDecorator(
            context,
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        /**
         * Adds a background color to both swiping directions
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addBackgroundColor(color: Int): Builder {
            mDecorator.setBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon to both swiping directions
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addActionIcon(drawableId: Int): Builder {
            mDecorator.setActionIconId(drawableId)
            return this
        }

        /**
         * Add a background color while swiping right
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightBackgroundColor(color: Int): Builder {
            mDecorator.setSwipeRightBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping right
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeRightActionIconId(drawableId)
            return this
        }

        /**
         * Adds a background color while swiping left
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftBackgroundColor(color: Int): Builder {
            mDecorator.setSwipeLeftBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping left
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeLeftActionIconId(drawableId)
            return this
        }

        /**
         * Set icon horizontal margin (default is 16dp)
         * @param pixels margin in pixels
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setIconHorizontalMargin(pixels: Int): Builder {
            mDecorator.setIconHorizontalMargin(pixels)
            return this
        }

        /**
         * Create a RecyclerViewSwipeDecorator
         * @return The created @RecyclerViewSwipeDecorator
         */
        fun create(): RecyclerViewSwipeDecorator {
            return mDecorator
        }

    }

}