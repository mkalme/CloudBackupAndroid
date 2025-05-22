package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.gui.view

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivity
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivityArgs
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

class SettingsLayout(private val layoutArgs: SettingsLayoutArgs) : LinearLayout(layoutArgs.context) {
    private lateinit var bucketEditText: EditText
    private lateinit var rootFolderEditText: EditText

    private var credentials = ""

    init {
        initializeComponent()
        setInput()
    }

    private fun initializeComponent() {
        View.inflate(context, R.layout.layout_cloud_provider_settings_google_storage, this)
        findViewById<Button>(R.id.layout_cloud_provider_settings_google_storage_editCredentialsButton).setOnClickListener { browse() }

        bucketEditText = findViewById(R.id.layout_cloud_provider_settings_google_storage_bucketText)
        rootFolderEditText = findViewById(R.id.layout_cloud_provider_settings_google_storage_rootFolderText)
    }

    private fun setInput(){
        bucketEditText.setText(layoutArgs.settings.bucket)
        rootFolderEditText.setText(layoutArgs.settings.rootFolder)
        credentials = layoutArgs.settings.accountCredentials
    }

    fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_google_storage_bucketTextHint).hint = languagePack.getTranslation("googleCloudProviderSettings.bucketTextHint")
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_google_storage_rootFolderTextHint).hint = languagePack.getTranslation("googleCloudProviderSettings.rootFolderTextHint")
        findViewById<TextView>(R.id.layout_cloud_provider_settings_google_storage_accountCredentialsTextView).text = languagePack.getTranslation("googleCloudProviderSettings.credentialsTextView")

        findViewById<Button>(R.id.layout_cloud_provider_settings_google_storage_editCredentialsButton).text = LanguageUtilities.translateEditable("googleCloudProviderSettings.editCredentialsButton", editable, languagePack)
    }

    fun setEditable(editable: Boolean) {
        bucketEditText.isEnabled = editable
        rootFolderEditText.isEnabled = editable
    }

    fun setSave() {
        layoutArgs.settings.invokeWithSingleEventTrigger {
            layoutArgs.settings.bucket = bucketEditText.text.toString()
            layoutArgs.settings.rootFolder = rootFolderEditText.text.toString()
            layoutArgs.settings.accountCredentials = credentials
        }
    }

    private fun browse() {
        val saveEvent = object: ArgsEvent<String> {
            override fun onInvoke(args: String) {
                credentials = args
            }
        }

        val args = TextEditorActivityArgs(credentials, layoutArgs.languagePermit.currentLanguage.getTranslation("googleCloudProviderSettings.editCredentialsActivity"), layoutArgs.editPermit, layoutArgs.languagePermit, saveEvent)
        Globals.setData("TextEditorActivity.Args", args)

        val intent = Intent(context, TextEditorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }
}