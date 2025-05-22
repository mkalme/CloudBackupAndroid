package com.storage.cloudbackup.gui.view.layout.image.button

import android.content.Context
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

data class ImageButtonLayoutArgs(
    val icon: Int,
    val clickEvent: EmptyEvent,
    val context: Context,
    val states: ImageButtonStateDrawableArgs = ImageButtonStateDrawableArgs(),
    val layoutId: Int? = null,
    val imageViewId: Int? = null)
