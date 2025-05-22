package com.storage.cloudbackup.gui.view.activity.scheme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.SpinnerItem
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.history.HistoryActivity
import com.storage.cloudbackup.gui.view.activity.history.HistoryActivityArgs
import com.storage.cloudbackup.gui.view.adapter.schedule.ScheduleAdapter
import com.storage.cloudbackup.gui.view.adapter.schedule.ScheduleAdapterArgs
import com.storage.cloudbackup.gui.view.dialog.filter.FilterDialog
import com.storage.cloudbackup.gui.view.dialog.filter.FilterDialogArgs
import com.storage.cloudbackup.gui.view.dialog.schedule.ScheduleDialog
import com.storage.cloudbackup.gui.view.dialog.schedule.ScheduleDialogArgs
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import com.storage.cloudbackup.logic.model.item.scheme.NetworkType
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.Reference
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericArgsProvider
import java.util.UUID

class SchemeActivity : AppCompatActivity() {
    private lateinit var activityArgs: SchemeActivityArgs

    private lateinit var filter: FileSearcher
    private val scheduleReferences = mutableListOf<Reference<Schedule>>()

    private lateinit var nameEditText: EditText
    private lateinit var driveFolderPathEditTex: EditText
    private lateinit var uploadNetworkTypeSpinner: Spinner
    private lateinit var comparisonMethodFileSizeCheckBox: CheckBox
    private lateinit var networkTypeAdapter: ArrayAdapter<SpinnerItem>
    private lateinit var adapter: ScheduleAdapter
    private lateinit var removeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("SchemeActivity.Args") as SchemeActivityArgs
        activityArgs.scheme.schedules.forEach {
            scheduleReferences.add(Reference(it.clone()))
        }
        filter = activityArgs.scheme.fileFilter.clone()

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent(){
        nameEditText = findViewById(R.id.activity_scheme_nameText)
        driveFolderPathEditTex = findViewById(R.id.activity_scheme_driveFolderText)
        uploadNetworkTypeSpinner = findViewById(R.id.activity_scheme_uploadNetworkTypeSpinner)
        comparisonMethodFileSizeCheckBox = findViewById(R.id.activity_scheme_fileSizeCheckBox)
        networkTypeAdapter = ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_list_item_1, mutableListOf())
        uploadNetworkTypeSpinner.adapter = networkTypeAdapter
        removeButton = findViewById(R.id.activity_scheme_removeThisButton)

        initRecyclerView()

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

    private fun initRecyclerView() {
        adapter = ScheduleAdapter(ScheduleAdapterArgs(scheduleReferences, activityArgs.editPermit, activityArgs.languagePermit, this))

        val view = findViewById<RecyclerView>(R.id.activity_scheme_scheduleListRecyclerView)
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)

        adapter.removedButtonClickListener = object: ArgsEvent<Reference<Schedule>> {
            override fun onInvoke(args: Reference<Schedule>) {
                scheduleRemoved(args)
            }
        }
    }

    private fun setInputs() {
        nameEditText.setText(activityArgs.scheme.name)
        driveFolderPathEditTex.setText(activityArgs.scheme.driveFolder)

        comparisonMethodFileSizeCheckBox.isChecked = activityArgs.scheme.useFileSizeForComparison

        setLanguage(activityArgs.editPermit.canEdit)

        val category = activityArgs.scheme.networkType.toString()
        ViewUtilities.selectTranslatedSpinnerItem(uploadNetworkTypeSpinner, category)

        setEditable(activityArgs.editPermit.canEdit)
    }

    private fun setLanguage(editable: Boolean) {
        val languagePack = activityArgs.languagePermit.currentLanguage

        networkTypeAdapter.clear()
        networkTypeAdapter.add(SpinnerItem(NetworkType.Wifi.toString(), languagePack.getTranslation("schemeActivity.networkTypeSpinner.wifi")))
        networkTypeAdapter.add(SpinnerItem(NetworkType.MobileData.toString(), languagePack.getTranslation("schemeActivity.networkTypeSpinner.mobileData")))
        networkTypeAdapter.add(SpinnerItem(NetworkType.Both.toString(), languagePack.getTranslation("schemeActivity.networkTypeSpinner.both")))
        networkTypeAdapter.notifyDataSetChanged()

        findViewById<TextInputLayout>(R.id.activity_scheme_nameTextHint).hint = languagePack.getTranslation("schemeActivity.nameTextHint")
        findViewById<TextInputLayout>(R.id.activity_scheme_driveFolderTextHint).hint = languagePack.getTranslation("schemeActivity.driveFolderHint")
        findViewById<TextView>(R.id.activity_scheme_networkTypeOnUploadTextView).text = languagePack.getTranslation("schemeActivity.networkTypeOnUploadTextView")
        findViewById<TextView>(R.id.activity_scheme_comparisonMethodTextView).text = languagePack.getTranslation("schemeActivity.comparisonMethodTextView")
        findViewById<CheckBox>(R.id.activity_scheme_fileNameCheckBox).text = languagePack.getTranslation("schemeActivity.fileNameCheckBox")
        findViewById<CheckBox>(R.id.activity_scheme_fileSizeCheckBox).text = languagePack.getTranslation("schemeActivity.fileSizeCheckBox")
        findViewById<TextView>(R.id.activity_scheme_seeHistoryButton).text = languagePack.getTranslation("schemeActivity.seeHistoryButton")
        findViewById<TextView>(R.id.activity_scheme_addNewScheduleButton).text = languagePack.getTranslation("schemeActivity.addNewScheduleButton")
        findViewById<TextView>(R.id.activity_scheme_saveButton).text = languagePack.getTranslation("schemeActivity.saveButton")
        findViewById<TextView>(R.id.activity_scheme_removeThisButton).text = languagePack.getTranslation("schemeActivity.removeThisButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        supportActionBar?.title = LanguageUtilities.translateEditable("schemeActivity.title", editable, languagePack)
        findViewById<Button>(R.id.activity_scheme_fileFilterButton).text = LanguageUtilities.translateEditable("schemeActivity.fileFilterButton", editable, languagePack)
        findViewById<Button>(R.id.activity_scheme_cancelButton).text = LanguageUtilities.translateEditable("schemeActivity.cancelButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        nameEditText.isEnabled = editable
        driveFolderPathEditTex.isEnabled = editable
        uploadNetworkTypeSpinner.isEnabled = editable
        comparisonMethodFileSizeCheckBox.isEnabled = editable

        findViewById<View>(R.id.activity_scheme_addNewScheduleButton).isEnabled = editable
        removeButton.isEnabled = editable
        removeButton.visibility = if (editable) View.VISIBLE else View.INVISIBLE
        findViewById<View>(R.id.activity_scheme_saveButton).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.schemeActivity_separator).visibility = if (editable) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun openFilterDialog(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        findViewById<View>(R.id.activity_scheme_fileFilterButton).isEnabled = false
        val closeEvent = object : EmptyEvent {
            override fun onInvoke() {
                findViewById<View>(R.id.activity_scheme_fileFilterButton).isEnabled = true
            }
        }

        val args = FilterDialogArgs(filter, activityArgs.editPermit, activityArgs.languagePermit, closeEvent)
        Globals.setData("FilterDialog.Args", args)

        val dialog = FilterDialog()
        dialog.show(supportFragmentManager, "")
    }

    private fun scheduleRemoved(scheduleReference: Reference<Schedule>) {
        ViewUtilities.showDeleteConfirmationDialog("schemeActivity.removeSchedulePrompt", activityArgs.languagePermit, supportFragmentManager, object: EmptyEvent {
            override fun onInvoke() {
                val index = scheduleReferences.indexOf(scheduleReference)
                scheduleReferences.remove(scheduleReference)
                adapter.notifyItemRemoved(index)
            }
        })
    }

    fun seeDates(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val schemeProvider = object: GenericArgsProvider<UUID, Scheme?> {
            override fun provide(key: UUID): Scheme {
                return activityArgs.scheme
            }
        }

        val clearEvent = object: EmptyEvent {
            override fun onInvoke() {
                activityArgs.scheme.history.updateAttempts.clear()
            }
        }

        val updateAttempts = activityArgs.scheme.history.updateAttempts.toMutableList()
        updateAttempts.sortByDescending { it.begin }

        val args = HistoryActivityArgs(updateAttempts, activityArgs.cloudProviders, schemeProvider, activityArgs.editPermit, activityArgs.languagePermit, clearEvent)
        Globals.setData("HistoryActivity.Args", args)

        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun addNewSchedule(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val closeEvent = object : EmptyEvent {
            override fun onInvoke() {}
        }

        val scheduleReference: Reference<Schedule> = Reference(DailySchedule())
        scheduleReferences.add(scheduleReference)
        adapter.notifyItemInserted(scheduleReferences.size - 1)

        val args = ScheduleDialogArgs(scheduleReference, activityArgs.editPermit, activityArgs.languagePermit, closeEvent)
        Globals.setData("ScheduleDialog.Args", args)

        val dialog = ScheduleDialog()
        dialog.show(supportFragmentManager, "Edit schedule")
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        if (!activityArgs.editPermit.canEdit) {
            finish()
            return
        }

        activityArgs.scheme.invokeWithSingleEventTrigger {
            activityArgs.scheme.name = nameEditText.text.toString()
            activityArgs.scheme.driveFolder = driveFolderPathEditTex.text.toString()
            activityArgs.scheme.fileFilter = filter

            val selectedItem = uploadNetworkTypeSpinner.selectedItem as SpinnerItem
            activityArgs.scheme.networkType = NetworkType.valueOf(selectedItem.id)

            activityArgs.scheme.useFileSizeForComparison = comparisonMethodFileSizeCheckBox.isChecked
            activityArgs.scheme.schedules.clear()
            for (i in scheduleReferences.indices) {
                activityArgs.scheme.schedules.add(scheduleReferences[i].getReference())
            }
        }

        finish()
    }
    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }

    fun removeThis(@Suppress("UNUSED_PARAMETER") ignoredView: View) {
        ViewUtilities.showDeleteConfirmationDialog("schemeActivity.removePrompt", activityArgs.languagePermit, supportFragmentManager, object: EmptyEvent {
            override fun onInvoke() {
                activityArgs.removeEvent.onInvoke(activityArgs.scheme)
                finish()
            }
        })
    }
}