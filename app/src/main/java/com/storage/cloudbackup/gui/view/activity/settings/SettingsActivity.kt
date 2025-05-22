package com.storage.cloudbackup.gui.view.activity.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.SpinnerItem
import com.storage.cloudbackup.gui.utilities.view.StorageUtilities
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import java.text.DecimalFormat

class SettingsActivity : AppCompatActivity() {
    private lateinit var activityArgs : SettingsActivityArgs

    private lateinit var editModeEnabledSwitch: SwitchCompat
    private lateinit var automaticUpdatingEnabledSwitch: SwitchCompat
    private lateinit var languageSpinner: Spinner

    private lateinit var monthlyMobileDataLimitEditText: EditText
    private lateinit var monthlyMobileDataLimitUnitSpinner: Spinner

    private lateinit var currentUsedDataContainerLinearLayout: LinearLayout
    private lateinit var currentUsedDataProgressLinearLayout: LinearLayout
    private lateinit var currentUsedDataPercentageTextView: TextView
    private lateinit var currentUsedDataTextView: TextView
    private lateinit var maxUsableDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("SettingsActivity.Args") as SettingsActivityArgs

        initializeComponent()
        setInput()
    }

    private fun initializeComponent(){
        editModeEnabledSwitch = findViewById(R.id.activity_settings_editModeEnabledSwitch)
        automaticUpdatingEnabledSwitch = findViewById(R.id.activity_settings_automaticUpdatingEnabledSwitch)
        languageSpinner = findViewById(R.id.activity_settings_languageSpinner)
        monthlyMobileDataLimitEditText = findViewById(R.id.activity_settings_monthlyMobileDataLimitEditText)
        monthlyMobileDataLimitUnitSpinner = findViewById(R.id.activity_settings_monthlyMobileDataLimitUnitSpinner)

        currentUsedDataContainerLinearLayout = findViewById(R.id.activity_settings_currentUsedDataContainerLinearLayout)
        currentUsedDataProgressLinearLayout = findViewById(R.id.activity_settings_currentUsedDataProgressLinearLayout)
        currentUsedDataPercentageTextView = findViewById(R.id.activity_settings_usedTextView)
        currentUsedDataTextView = findViewById(R.id.activity_settings_currentUsedDataTextView)
        maxUsableDataTextView = findViewById(R.id.activity_settings_maxUsableDataTextView)

        monthlyMobileDataLimitUnitSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, DataUnit.entries.map { it.toString() })

        val vto = currentUsedDataProgressLinearLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener { setUsageControl() }

        val languageAdapter = ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_list_item_1, mutableListOf())
        activityArgs.settings.languagePackContainer.packs.forEach{
            languageAdapter.add(SpinnerItem(it.value.code, it.value.name))
        }
        languageSpinner.adapter = languageAdapter
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                setUsageControl()
                setLanguage()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setInput(){
        editModeEnabledSwitch.isChecked = activityArgs.settings.editModeEnabled
        automaticUpdatingEnabledSwitch.isChecked = activityArgs.settings.automaticUpdatingEnabled
        monthlyMobileDataLimitEditText.setText(activityArgs.settings.monthlyMobileDataLimit.amount.toString())
        ViewUtilities.selectTranslatedSpinnerItem(languageSpinner, activityArgs.settings.languagePack)

        val category: String = activityArgs.settings.monthlyMobileDataLimit.unit.toString()
        ViewUtilities.selectSpinnerItem(monthlyMobileDataLimitUnitSpinner, category)

        setUsageControl()
    }

    private fun setUsageControl() {
        val selectedLanguage = languageSpinner.selectedItem as SpinnerItem
        val languagePack = activityArgs.settings.languagePackContainer.packs[selectedLanguage.id]!!

        val usedTranslation = languagePack.getTranslation("settingsActivity.usedText")
        val overLimitTranslation = languagePack.getTranslation("settingsActivity.overLimitText")

        val usage: ULong = activityArgs.usedData
        var percentage = usage.toDouble() / activityArgs.settings.monthlyMobileDataLimit.getLongSize().toDouble()

        val df = DecimalFormat()
        df.maximumFractionDigits = 2

        val usedMessage = "${usedTranslation}: ${df.format(percentage * 100)}%${if(percentage > 1) " (${overLimitTranslation})" else ""}"
        currentUsedDataPercentageTextView.text = usedMessage

        if (percentage > 1) percentage = 1.0

        currentUsedDataProgressLinearLayout.layoutParams.width = (percentage * currentUsedDataContainerLinearLayout.width).toInt()
        currentUsedDataProgressLinearLayout.layoutParams = currentUsedDataProgressLinearLayout.layoutParams

        currentUsedDataTextView.text = StorageUtilities.convertToStorage(usage.toLong(), df)
        maxUsableDataTextView.text = StorageUtilities.convertToStorage(
            activityArgs.settings.monthlyMobileDataLimit.getLongSize().toLong(), df
        )
    }

    private fun setLanguage(){
        val selectedLanguage = languageSpinner.selectedItem as SpinnerItem
        val languagePack = activityArgs.settings.languagePackContainer.packs[selectedLanguage.id]!!

        supportActionBar?.title = languagePack.getTranslation("settingsActivity.title")
        findViewById<SwitchCompat>(R.id.activity_settings_editModeEnabledSwitch).text = languagePack.getTranslation("settingsActivity.editModeEnabledSwitch")
        findViewById<SwitchCompat>(R.id.activity_settings_automaticUpdatingEnabledSwitch).text = languagePack.getTranslation("settingsActivity.automaticUpdatingEnabledSwitch")
        findViewById<TextView>(R.id.activity_settings_languageText).text = languagePack.getTranslation("settingsActivity.languageText")
        findViewById<TextView>(R.id.activity_settings_monthlyMobileDataLimitTextView).text = languagePack.getTranslation("settingsActivity.monthlyMobileDataLimitTextView")
        findViewById<Button>(R.id.activity_settings_saveButton).text = languagePack.getTranslation("settingsActivity.saveButton")
        findViewById<Button>(R.id.activity_settings_cancelButton).text = languagePack.getTranslation("settingsActivity.cancelButton")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        activityArgs.settings.invokeWithSingleEventTrigger {
            activityArgs.settings.editModeEnabled = editModeEnabledSwitch.isChecked
            activityArgs.settings.automaticUpdatingEnabled = automaticUpdatingEnabledSwitch.isChecked

            val selectedLanguage = languageSpinner.selectedItem as SpinnerItem
            activityArgs.settings.languagePack = selectedLanguage.id

            val unit = DataUnit.valueOf(monthlyMobileDataLimitUnitSpinner.selectedItem.toString())
            val amount = monthlyMobileDataLimitEditText.text.toString().toULong()
            activityArgs.settings.monthlyMobileDataLimit = DataAmount(unit, amount)
        }

        finish()
    }

    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}