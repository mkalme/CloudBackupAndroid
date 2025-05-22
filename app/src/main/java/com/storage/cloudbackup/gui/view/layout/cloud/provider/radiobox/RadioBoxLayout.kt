package com.storage.cloudbackup.gui.view.layout.cloud.provider.radiobox

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import com.storage.cloudbackup.R

class RadioBoxLayout(val layoutArgs: RadioBoxLayoutArgs) : LinearLayout(layoutArgs.context) {
    private lateinit var iconImageView: ImageView

    private var backgroundNonSelectedUp = getDrawable("#262626")
    private var backgroundNonSelectedDown = getDrawable("#484848")

    private var backgroundSelectedUp = getDrawable("#606060", "#AAAAAA",2f)
    private var backgroundSelectedDown = getDrawable("#808080","#AAAAAA",2f)

    private var isDown = false

    private var _isProviderSelected = false
    private var isProviderSelected: Boolean
        get() = _isProviderSelected
        private set(value){
            setIsProviderSelected(value, false)
        }

    fun setIsProviderSelected(isSelected: Boolean, auto: Boolean){
        val old = _isProviderSelected
        _isProviderSelected = isSelected

        if(old != _isProviderSelected){
            background = setBackground(isDown)
            layoutArgs.selectedListener.onInvoke(RadioBoxEventArgs(_isProviderSelected, auto))
        }
    }

    init {
        initializeComponent()
    }

    private fun initializeComponent(){
        View.inflate(context, R.layout.layout_cloud_provider_radiobox, this)

        iconImageView = findViewById(R.id.layout_cloud_provider_radio_box_icon)
        iconImageView.setImageResource(layoutArgs.cloudProvider.logicComponent.info.icon)

        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                backgroundNonSelectedUp = getDrawable("#FFFFFF", "#AAAAAA")
                backgroundNonSelectedDown = getDrawable("#DDDDDD", "#AAAAAA")

                backgroundSelectedUp = getDrawable("#DCDCDC", "#555555",2f)
                backgroundSelectedDown = getDrawable("#BBBBBB","#555555",2f)
            }
        }

        background = backgroundNonSelectedUp
        setListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListener(){
        val listener = OnTouchListener { _: View?, event: MotionEvent ->
            if(!layoutArgs.editPermit.canEdit) return@OnTouchListener false

            val x = event.x.toInt()
            val y = event.y.toInt()
            val inside = x in 0..width && y in 0 .. height

            val e = event.action
            isDown = e == MotionEvent.ACTION_DOWN || e == MotionEvent.ACTION_MOVE

            if(isDown){
                background = setBackground(true)
            }else{
                if(inside) isProviderSelected = !isProviderSelected
                else background = setBackground(false)
            }

            true
        }

        setOnTouchListener(listener)
    }

    private fun getDrawable(color: String, borderColor: String = "#444444", den: Float = 1.0f): GradientDrawable {
        val density = context.resources.displayMetrics.density

        return GradientDrawable().apply {
            setColor(Color.parseColor(color))
            setStroke((den * density).toInt(), Color.parseColor(borderColor))
        }
    }

    private fun setBackground(holdDown: Boolean): Drawable {
        return if(holdDown){
            if(isProviderSelected) backgroundSelectedDown else backgroundNonSelectedDown
        }else{
            if(isProviderSelected) backgroundSelectedUp else backgroundNonSelectedUp
        }
    }
}