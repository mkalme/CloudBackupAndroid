package com.storage.cloudbackup.gui.view.dialog.filter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class FilterDialog : AppCompatDialogFragment() {
    private lateinit var dialogArgs: FilterDialogArgs

    private lateinit var filterEditText: EditText
    private lateinit var minFileSizeEditText: EditText
    private lateinit var minFileSizeUnitSpinner: Spinner
    private lateinit var maxFileSizeEditText: EditText
    private lateinit var maxFileSizeUnitSpinner: Spinner
    private lateinit var okButton: Button
    private lateinit var negativeButton: Button

    private lateinit var view: View
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogArgs = Globals.getData("FilterDialog.Args") as FilterDialogArgs

        initializeComponent()
        setInput()
        setEditableLanguage(dialogArgs.editPermit.canEdit)

        return dialog
    }

    @SuppressLint("InflateParams")
    private fun initializeComponent() {
        val inflater: LayoutInflater = requireActivity().layoutInflater
        builder = AlertDialog.Builder(activity, R.style.Style_CloudBackup_Dialog)
        view = inflater.inflate(R.layout.dialog_filter, null, false)

        filterEditText = view.findViewById(R.id.dialog_filter_filterEditText)
        minFileSizeEditText = view.findViewById(R.id.dialog_filter_minFileSizeEditText)
        minFileSizeUnitSpinner = view.findViewById(R.id.dialog_filter_minFileSizeUnitSpinner)
        maxFileSizeEditText = view.findViewById(R.id.dialog_filter_maxFileSizeEditText)
        maxFileSizeUnitSpinner = view.findViewById(R.id.dialog_filter_maxFileSizeUnitSpinner)

        minFileSizeUnitSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, DataUnit.entries.map { it.toString()})
        maxFileSizeUnitSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, DataUnit.entries.map { it.toString()})

        builder.setView(view)
        builder.setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
            dialogArgs.closeEvent.onInvoke()
        }
        builder.setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
            save()
            dialogArgs.closeEvent.onInvoke()
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
            okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            setLanguage(dialogArgs.editPermit.canEdit)
            setEditable(dialogArgs.editPermit.canEdit)
        }

        this.dialog = dialog
    }

    private fun setInput() {
        filterEditText.setText(dialogArgs.filter.fileNameFilter)

        minFileSizeEditText.setText(java.lang.String.valueOf(dialogArgs.filter.minFileSize.amount))
        setSpinnerCategory(minFileSizeUnitSpinner, dialogArgs.filter.minFileSize.unit)

        maxFileSizeEditText.setText(java.lang.String.valueOf(dialogArgs.filter.maxFileSize.amount))
        setSpinnerCategory(maxFileSizeUnitSpinner, dialogArgs.filter.maxFileSize.unit)
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage

        dialog.findViewById<TextView>(R.id.dialog_filter_fileNameFilterTextView).text = languagePack.getTranslation("fileFilterDialog.fileNameFilterTextView")
        dialog.findViewById<TextView>(R.id.dialog_filter_minimumFileSizeTextView).text = languagePack.getTranslation("fileFilterDialog.minimumFileSizeTextView")
        dialog.findViewById<TextView>(R.id.dialog_filter_maximumFileSizeTextView).text = languagePack.getTranslation("fileFilterDialog.maximumFileSizeTextView")

        negativeButton.text = languagePack.getTranslation("fileFilterDialog.cancelButton")
        okButton.text = languagePack.getTranslation("fileFilterDialog.okButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage

        dialog.setTitle(LanguageUtilities.translateEditable("fileFilterDialog.title", editable, languagePack))
    }

    private fun setEditable(editable: Boolean) {
        filterEditText.isEnabled = editable
        minFileSizeEditText.isEnabled = editable
        minFileSizeUnitSpinner.isEnabled = editable
        maxFileSizeEditText.isEnabled = editable
        maxFileSizeUnitSpinner.isEnabled = editable
        negativeButton.visibility = if (editable) View.VISIBLE else View.INVISIBLE
    }

    private fun setSpinnerCategory(spinner: Spinner?, unit: DataUnit) {
        val category: String = unit.toString().uppercase()
        ViewUtilities.selectSpinnerItem(spinner!!, category)
    }

    override fun onCancel(dialog: DialogInterface) {
        dialogArgs.closeEvent.onInvoke()
    }

    private fun save() {
        if (!dialogArgs.editPermit.canEdit) return

        dialogArgs.filter.invokeWithSingleEventTrigger {
            dialogArgs.filter.fileNameFilter = filterEditText.text.toString()
            dialogArgs.filter.minFileSize = DataAmount(DataUnit.valueOf(minFileSizeUnitSpinner.selectedItem.toString()), minFileSizeEditText.text.toString().toULong())
            dialogArgs.filter.maxFileSize = DataAmount(DataUnit.valueOf(maxFileSizeUnitSpinner.selectedItem.toString()), maxFileSizeEditText.text.toString().toULong())
        }
    }
}