package com.storage.cloudbackup.gui.view.activity.cloud.provider

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class SettingsActivity : AppCompatActivity() {
    private lateinit var activityArgs : SettingsActivityArgs

    private lateinit var editorLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_provider_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("CloudProviderSettingsActivity.Args") as SettingsActivityArgs

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent() {
        editorLayout = findViewById(R.id.activity_cloud_provider_settings_editLinearLayout)
        editorLayout.addView(activityArgs.cloudProvider.guiComponent.settings!!.getSettingsView())

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
    }

    private fun setInputs() {
        setLanguage(activityArgs.editPermit.canEdit)
        setEditable(activityArgs.editPermit.canEdit)
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage
        findViewById<Button>(R.id.activity_cloud_provider_settings_saveButton).text = languagePack.getTranslation("cloudProviderSettingsActivity.saveButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        activityArgs.cloudProvider.guiComponent.settings!!.setLanguage(languagePack, editable)

        val title = "${LanguageUtilities.translateEditable("cloudProviderSettingsActivity.title", editable, languagePack)} ${activityArgs.cloudProvider.logicComponent.info.name}"
        supportActionBar?.title = title

        findViewById<Button>(R.id.activity_cloud_provider_settings_cancelButton).text = LanguageUtilities.translateEditable("cloudProviderSettingsActivity.cancelButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        activityArgs.cloudProvider.guiComponent.settings!!.setEditable(editable)
        findViewById<View>(R.id.activity_cloud_provider_settings_saveButton).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.activity_cloud_provider_settings_separator).visibility = if (editable) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        activityArgs.cloudProvider.guiComponent.settings!!.setSave()
        finish()
    }

    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}