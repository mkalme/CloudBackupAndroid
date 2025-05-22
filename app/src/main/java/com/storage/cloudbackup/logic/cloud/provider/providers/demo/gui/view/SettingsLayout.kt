package com.storage.cloudbackup.logic.cloud.provider.providers.demo.gui.view

import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivity
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivityArgs
import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

class SettingsLayout(private val layoutArgs: SettingsLayoutArgs) : LinearLayout(layoutArgs.context) {
    private lateinit var rootFolderEditText: EditText
    private lateinit var simulatedUploadRateEditText: EditText
    private lateinit var simulatedUploadRateSpinner: Spinner
    private lateinit var simulatedLatencyEditText: EditText
    private lateinit var introduceRandomErrorsSwitch: SwitchCompat
    private lateinit var trackUploadedFilesSwitch: SwitchCompat

    private lateinit var files: List<File>

    init {
        initializeComponent()
        setInput()
    }

    private fun initializeComponent() {
        View.inflate(context, R.layout.layout_cloud_provider_settings_demo, this)
        findViewById<Button>(R.id.layout_cloud_provider_settings_demo_editUploadedFilesButton).setOnClickListener { editFiles() }
        findViewById<Button>(R.id.layout_cloud_provider_settings_demo_clearUploadedFilesButton).setOnClickListener { clear() }

        rootFolderEditText = findViewById(R.id.layout_cloud_provider_settings_demo_rootFolderText)
        simulatedUploadRateEditText = findViewById(R.id.layout_cloud_provider_settings_demo_simulatedUploadTextEdit)
        simulatedUploadRateSpinner = findViewById(R.id.layout_cloud_provider_settings_demo_simulatedSizeUnitSizeUnitSpinner)
        simulatedLatencyEditText = findViewById(R.id.layout_cloud_provider_settings_demo_simulatedLatencyTextEdit)
        introduceRandomErrorsSwitch = findViewById(R.id.layout_cloud_provider_settings_demo_introduceRandomErrors)
        trackUploadedFilesSwitch = findViewById(R.id.layout_cloud_provider_settings_demo_trackUploadedFiles)

        simulatedUploadRateSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, DataUnit.entries.map { it.toString() })
    }

    private fun setInput(){
        rootFolderEditText.setText(layoutArgs.settings.rootFolder)
        simulatedUploadRateEditText.setText(layoutArgs.settings.simulatedUploadRate.amount.toString())

        val category: String = layoutArgs.settings.simulatedUploadRate.unit.toString()
        ViewUtilities.selectSpinnerItem(simulatedUploadRateSpinner, category)

        simulatedLatencyEditText.setText(layoutArgs.settings.simulatedLatencyMilliseconds.toString())
        introduceRandomErrorsSwitch.isChecked = layoutArgs.settings.introduceRandomErrors
        trackUploadedFilesSwitch.isChecked = layoutArgs.settings.trackUploadedFiles

        files = layoutArgs.settings.simulatedUploadedFiles
    }

    fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_demo_rootFolderTextHint).hint = languagePack.getTranslation("demoCloudProviderSettings.rootFolderTextHint")
        findViewById<TextView>(R.id.layout_cloud_provider_settings_demo_simulatedUploadTextView).text = languagePack.getTranslation("demoCloudProviderSettings.simulatedUploadRateTextView")
        findViewById<TextView>(R.id.layout_cloud_provider_settings_demo_simulatedLatencyTextView).text = languagePack.getTranslation("demoCloudProviderSettings.simulatedLatencyTextView")
        introduceRandomErrorsSwitch.text = languagePack.getTranslation("demoCloudProviderSettings.introduceRandomErrors")
        trackUploadedFilesSwitch.text = languagePack.getTranslation("demoCloudProviderSettings.trackUploadedFiles")

        setUploadedFilesTextViewText()
        findViewById<Button>(R.id.layout_cloud_provider_settings_demo_editUploadedFilesButton).text = LanguageUtilities.translateEditable("demoCloudProviderSettings.editUploadedFilesButton", editable, languagePack)
        findViewById<Button>(R.id.layout_cloud_provider_settings_demo_clearUploadedFilesButton).text = languagePack.getTranslation("demoCloudProviderSettings.clearUploadedFilesButton")
    }

    fun setEditable(editable: Boolean) {
        rootFolderEditText.isEnabled = editable
        simulatedUploadRateEditText.isEnabled = editable
        simulatedLatencyEditText.isEnabled = editable
        introduceRandomErrorsSwitch.isEnabled = editable
        trackUploadedFilesSwitch.isEnabled = editable
        simulatedUploadRateSpinner.isEnabled = editable
        findViewById<Button>(R.id.layout_cloud_provider_settings_demo_clearUploadedFilesButton).isEnabled = editable
    }

    private fun setUploadedFilesTextViewText(){
        val languagePack = layoutArgs.languagePermit.currentLanguage

        val translation = languagePack.getTranslation("demoCloudProviderSettings.uploadedFilesTextView")
        val text = "$translation: ${files.size}"

        findViewById<TextView>(R.id.layout_cloud_provider_settings_demo_editUploadedFilesTextView).text = text
    }

    fun setSave() {
        layoutArgs.settings.invokeWithSingleEventTrigger {
            layoutArgs.settings.rootFolder = rootFolderEditText.text.toString()

            val unit = DataUnit.valueOf(simulatedUploadRateSpinner.selectedItem.toString())
            val amount = simulatedUploadRateEditText.text.toString().toULong()
            layoutArgs.settings.simulatedUploadRate = DataAmount(unit, amount)

            layoutArgs.settings.simulatedLatencyMilliseconds = simulatedLatencyEditText.text.toString().toInt()
            layoutArgs.settings.introduceRandomErrors = introduceRandomErrorsSwitch.isChecked
            layoutArgs.settings.trackUploadedFiles = trackUploadedFilesSwitch.isChecked

            layoutArgs.settings.simulatedUploadedFiles.clear()
            layoutArgs.settings.simulatedUploadedFiles.addAll(files)
        }
    }

    private fun editFiles() {
        val saveEvent = object: ArgsEvent<String> {
            override fun onInvoke(args: String) {
                val split = args.split("\n")

                val output = mutableListOf<File>()
                split.forEach {
                    try{
                        val colanSplit = it.split(":")
                        val file = File(colanSplit[0], colanSplit[1].toULong())
                        output.add(file)
                    }catch (ignored: Exception){}
                }

                files = output
                setUploadedFilesTextViewText()
            }
        }

        val builder = StringBuilder()
        files.forEach {
            builder.appendLine("${it.name}:${it.size}")
        }

        val args = TextEditorActivityArgs(builder.toString(), layoutArgs.languagePermit.currentLanguage.getTranslation("demoCloudProviderSettings.editUploadedFilesActivity"), layoutArgs.editPermit, layoutArgs.languagePermit, saveEvent)
        Globals.setData("TextEditorActivity.Args", args)

        val intent = Intent(context, TextEditorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }

    private fun clear() {
        files = mutableListOf()
        setUploadedFilesTextViewText()
    }
}