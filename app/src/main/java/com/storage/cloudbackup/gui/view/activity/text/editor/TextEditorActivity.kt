package com.storage.cloudbackup.gui.view.activity.text.editor

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.view.layout.image.button.ImageButtonLayout
import com.storage.cloudbackup.gui.view.layout.image.button.ImageButtonLayoutArgs
import com.storage.cloudbackup.gui.view.layout.image.button.ImageButtonStateDrawableArgs
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class TextEditorActivity : AppCompatActivity() {
    private lateinit var activityArgs: TextEditorActivityArgs

    private lateinit var editText: EditText
    private var textSize = 30f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_editor)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("TextEditorActivity.Args") as TextEditorActivityArgs

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent(){
        editText = findViewById(R.id.activity_text_editor_editText)

        activityArgs.editPermit.editListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setEditable(activityArgs.editPermit.canEdit)
                setEditableLanguage(activityArgs.editPermit.canEdit)
            }
        })

        activityArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(activityArgs.editPermit.canEdit)
            }
        })

        val zoomInClickEvent = object: EmptyEvent {
            override fun onInvoke() {
                textSize *= 1 / 0.95f
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
        }

        val states = ImageButtonStateDrawableArgs(
            R.drawable.background_button_image_text_up_light,
            R.drawable.background_button_image_text_down_light,
            R.drawable.background_button_image_text_up_dark,
            R.drawable.background_button_image_text_down_dark )

        val zoomInLayout = ImageButtonLayout(ImageButtonLayoutArgs(R.mipmap.activity_text_editor_zoom_in, zoomInClickEvent, this, states, R.layout.layout_image_text_editor_button, R.id.layout_image_text_editor_button_icon))
        setScale(zoomInLayout)
        findViewById<LinearLayout>(R.id.activity_text_editor_zoomInLayout).addView(zoomInLayout)

        val zoomOutClickEvent = object: EmptyEvent {
            override fun onInvoke() {
                textSize *= 0.95f
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
        }

        val zoomOutLayout = ImageButtonLayout(ImageButtonLayoutArgs(R.mipmap.activity_text_editor_zoom_out, zoomOutClickEvent, this, states, R.layout.layout_image_text_editor_button, R.id.layout_image_text_editor_button_icon))
        setScale(zoomOutLayout)
        findViewById<LinearLayout>(R.id.activity_text_editor_zoomOutLayout).addView(zoomOutLayout)
    }

    private fun setScale(layout: LinearLayout){
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        layout.layoutParams = layoutParams
    }

    private fun setInputs() {
        editText.setText(activityArgs.text)

        setLanguage(activityArgs.editPermit.canEdit)
        setEditable(activityArgs.editPermit.canEdit)
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        findViewById<Button>(R.id.activity_text_editor_saveButton).text = languagePack.getTranslation("updatePlanActivity.saveButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        val title = "${LanguageUtilities.translateEditable("textEditorActivity.title", editable, languagePack)} ${activityArgs.title}"

        supportActionBar?.title = title
        findViewById<Button>(R.id.activity_text_editor_cancelButton).text = LanguageUtilities.translateEditable("textEditorActivity.cancelButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        editText.isFocusableInTouchMode = editable
        editText.clearFocus()

        val enabledColor: Int
        val disabledColor: Int

        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> {
                enabledColor = getColor(R.color.cloud_backup_activity_text_editor_text_enabled_light)
                disabledColor = getColor(R.color.cloud_backup_activity_text_editor_text_disabled_light)
            }
            else -> {
                enabledColor = getColor(R.color.cloud_backup_activity_text_editor_text_enabled_dark)
                disabledColor = getColor(R.color.cloud_backup_activity_text_editor_text_disabled_dark)
            }
        }

        editText.setTextColor(if(editable) enabledColor else disabledColor)

        findViewById<View>(R.id.activity_text_editor_saveButton).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.activity_text_editor_separator).visibility = if (editable) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        activityArgs.saveEvent.onInvoke(editText.text.toString())
        finish()
    }

    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}