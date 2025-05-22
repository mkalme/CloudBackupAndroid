package com.storage.cloudbackup.gui.view.layout.image.button

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.background.Background
import com.storage.cloudbackup.gui.utilities.background.BackgroundUtilities

class ImageButtonLayout(private val layoutArgs: ImageButtonLayoutArgs) : LinearLayout(layoutArgs.context) {
    private lateinit var iconImageView: ImageView

    init {
        initializeComponent()
    }

    private fun initializeComponent(){
        val layoutId = layoutArgs.layoutId ?: R.layout.layout_image_button
        val iconId = layoutArgs.imageViewId ?: R.id.layout_image_button_icon

        View.inflate(context, layoutId, this)

        iconImageView = findViewById(iconId)
        iconImageView.setImageResource(layoutArgs.icon)

        val backgroundUp: Drawable?
        val backgroundDown: Drawable?

        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                backgroundUp = ContextCompat.getDrawable(context, layoutArgs.states.lightUp)
                backgroundDown = ContextCompat.getDrawable(context, layoutArgs.states.lightDown)
            }
            else -> {
                backgroundUp = ContextCompat.getDrawable(context, layoutArgs.states.darkUp)
                backgroundDown = ContextCompat.getDrawable(context, layoutArgs.states.darkDown)
            }
        }

        val background = Background(backgroundUp!!, backgroundDown!!)

        this.setOnClickListener {
            layoutArgs.clickEvent.onInvoke()
        }

        BackgroundUtilities.setBackgroundOnTouch(this, background)
    }
}