package com.storage.cloudbackup.gui.utilities.view

import android.R.attr.value
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.logic.permit.edit.EmptyEditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.gui.view.dialog.prompt.PromptDialog
import com.storage.cloudbackup.gui.view.dialog.prompt.PromptDialogArgs
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

object ViewUtilities {
    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener(colorArgs: ViewTouchArgs, parent: View, vararg childNodes: View) {
        val listener = OnTouchListener { _: View?, event: MotionEvent ->
            val e = event.action

            val color: Int = if (e == MotionEvent.ACTION_DOWN || e == MotionEvent.ACTION_MOVE) colorArgs.down else colorArgs.up
            parent.setBackgroundColor(color)
            for (i in childNodes.indices) {
                childNodes[i].setBackgroundColor(color)
            }
            false
        }

        parent.setOnTouchListener(listener)
    }

    fun getAdapterColor(context: Context): ViewTouchArgs {
        return when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> ViewTouchArgs(
                context.getColor(R.color.cloud_backup_adapter_background_up_light),
                context.getColor(R.color.cloud_backup_adapter_background_down_light))
            else -> ViewTouchArgs(
                context.getColor(R.color.cloud_backup_adapter_background_up_dark),
                context.getColor(R.color.cloud_backup_adapter_background_down_dark))
        }
    }

    fun showDeleteConfirmationDialog(translationKey: String, languagePermit: LanguagePermit, fragmentManager: FragmentManager, clickEvent: EmptyEvent) {
        val deletionClickEvent = object : ArgsEvent<Boolean> {
            override fun onInvoke(args: Boolean) {
                if(args) clickEvent.onInvoke()
            }
        }

        val args = PromptDialogArgs(EmptyEditPermit(true), languagePermit, languagePermit.currentLanguage.getTranslation(translationKey),  Gravity.CENTER_HORIZONTAL, deletionClickEvent)
        Globals.setData("PromptDialog.Args", args)

        val dialog = PromptDialog()
        dialog.show(fragmentManager, "")
    }

    fun selectSpinnerItem(spinner: Spinner, item: String?) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).equals(item)) {
                spinner.setSelection(i)
                return
            }
        }
    }

    fun selectTranslatedSpinnerItem(spinner: Spinner, id: String) {
        for (i in 0 until spinner.count) {
            val item = spinner.getItemAtPosition(i) as SpinnerItem

            if (item.id == id) {
                spinner.setSelection(i)
                return
            }
        }
    }

    fun translateEnum(key: String, languagePack: LanguagePack, value: String): String{
        val fullKey = "$key.${value.first().lowercaseChar()}${value.drop(1)}"
        return languagePack.getTranslation(fullKey)
    }
}