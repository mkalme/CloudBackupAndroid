package com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui.view

import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class SettingsLayout(val layoutArgs: SettingsLayoutArgs) : LinearLayout(layoutArgs.context) {
    private lateinit var bucketEditText: EditText
    private lateinit var rootFolderEditText: EditText

    private lateinit var accessKeyEditText: EditText
    private lateinit var secretKeyEditText: EditText

    init {
        initializeComponent()
        setInput()
    }

    private fun initializeComponent() {
        View.inflate(context, R.layout.layout_cloud_provider_settings_s3, this)

        bucketEditText = findViewById(R.id.layout_cloud_provider_settings_s3_bucketText)
        rootFolderEditText = findViewById(R.id.layout_cloud_provider_settings_s3_rootFolderText)
        accessKeyEditText = findViewById(R.id.layout_cloud_provider_settings_s3_accessKeyText)
        secretKeyEditText = findViewById(R.id.layout_cloud_provider_settings_s3_secretKeyText)
    }

    private fun setInput(){
        bucketEditText.setText(layoutArgs.settings.bucket)
        rootFolderEditText.setText(layoutArgs.settings.rootFolder)

        accessKeyEditText.setText(layoutArgs.settings.accessKey)
        secretKeyEditText.setText(layoutArgs.settings.secretKey)

    }

    fun setLanguage(languagePack: LanguagePack) {
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_s3_bucketTextHint).hint = languagePack.getTranslation("amazonCloudProviderSettings.bucketTextHint")
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_s3_rootFolderTextHint).hint = languagePack.getTranslation("amazonCloudProviderSettings.rootFolderTextHint")
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_s3_accessKeyTextHint).hint = languagePack.getTranslation("amazonCloudProviderSettings.accessKeyTextHint")
        findViewById<TextInputLayout>(R.id.layout_cloud_provider_settings_s3_secretKeyTextHint).hint = languagePack.getTranslation("amazonCloudProviderSettings.secretKeyTextHint")
    }

    fun setEditable(editable: Boolean) {
        bucketEditText.isEnabled = editable
        rootFolderEditText.isEnabled = editable
        accessKeyEditText.isEnabled = editable
        secretKeyEditText.isEnabled = editable
    }

    fun setSave() {
        layoutArgs.settings.invokeWithSingleEventTrigger {
            layoutArgs.settings.bucket = bucketEditText.text.toString()
            layoutArgs.settings.rootFolder = rootFolderEditText.text.toString()
            layoutArgs.settings.accessKey = accessKeyEditText.text.toString()
            layoutArgs.settings.secretKey = secretKeyEditText.text.toString()
        }
    }
}