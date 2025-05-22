package com.storage.cloudbackup.gui.utilities.background

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

object BackgroundUtilities {
    @SuppressLint("ClickableViewAccessibility")
    fun setBackgroundOnTouch(parent: View, background: Background) {
        parent.background = background.upBorder

        val listener = View.OnTouchListener { _: View?, event: MotionEvent ->
            val e = event.action
            val drawable = if (e == MotionEvent.ACTION_DOWN || e == MotionEvent.ACTION_MOVE) background.downBorder else background.upBorder

            parent.background = drawable
            false
        }

        parent.setOnTouchListener(listener)
    }
}