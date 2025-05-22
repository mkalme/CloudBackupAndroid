package com.storage.cloudbackup.gui.view.dialog.prompt

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class PromptDialog : AppCompatDialogFragment() {
    private lateinit var dialogArgs: PromptDialogArgs

    private lateinit var okButton: Button
    private lateinit var negativeButton: Button

    private lateinit var view: View
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogArgs = Globals.getData("PromptDialog.Args") as PromptDialogArgs

        initializeComponent()

        return dialog
    }

    @SuppressLint("InflateParams")
    private fun initializeComponent() {
        val inflater: LayoutInflater = requireActivity().layoutInflater
        builder = AlertDialog.Builder(activity, R.style.Style_CloudBackup_Dialog)
        view = inflater.inflate(R.layout.dialog_prompt, null, false)

        builder.setView(view)
        builder.setNegativeButton("Yes") { _: DialogInterface?, _: Int ->
            dialogArgs.clickEvent?.onInvoke(true)
        }
        builder.setPositiveButton("No") { _: DialogInterface?, _: Int ->
            dialogArgs.clickEvent?.onInvoke(false)
        }

        dialogArgs.editPermit.editListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setEditable(dialogArgs.editPermit.canEdit)
                setEditableLanguage(dialogArgs.editPermit.canEdit)
            }
        })

        dialogArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(dialogArgs.editPermit.canEdit)
                setEditableLanguage(dialogArgs.editPermit.canEdit)
            }
        })
        val dialog = builder.create()
        dialog.setOnShowListener { _: DialogInterface? ->
            okButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            setLanguage(dialogArgs.editPermit.canEdit)
            setEditable(dialogArgs.editPermit.canEdit)
        }

        this.dialog = dialog
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage

        val textView = dialog.findViewById<TextView>(R.id.dialog_prompt_textView)
        textView.text = dialogArgs.message
        textView.gravity = dialogArgs.gravity

        okButton.text = languagePack.getTranslation("promptDialog.yesButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage
        negativeButton.text = LanguageUtilities.translateEditable("promptDialog.noButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        okButton.visibility = if (editable) View.VISIBLE else View.INVISIBLE
    }

    override fun onCancel(dialog: DialogInterface) {
        dialogArgs.clickEvent?.onInvoke(false)
    }
}