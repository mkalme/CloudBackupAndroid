package com.storage.cloudbackup.gui.view.dialog.schedule

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatDialogFragment
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.SpinnerItem
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.dialog.schedule.variant.DailyEditor
import com.storage.cloudbackup.gui.view.dialog.schedule.variant.ScheduleEditor
import com.storage.cloudbackup.gui.view.layout.schedule.MonthlyEditorLayout
import com.storage.cloudbackup.gui.view.layout.schedule.WeeklyEditorLayout
import com.storage.cloudbackup.gui.view.layout.schedule.YearlyEditorLayout
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.ScheduleType
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import java.text.DecimalFormat
import java.util.EnumMap

class ScheduleDialog : AppCompatDialogFragment() {
    private lateinit var dialogArgs: ScheduleDialogArgs

    private lateinit var skipEditText: EditText
    private lateinit var intervalUnitSpinner: Spinner
    private lateinit var intervalAdapter: ArrayAdapter<SpinnerItem>
    private lateinit var timeOfDayButton: Button
    private lateinit var scheduleEditorLayout: LinearLayout
    private lateinit var scheduleTimeOfDayEditText: EditText
    private lateinit var okButton: Button
    private lateinit var negativeButton: Button

    private lateinit var view: View
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private lateinit var scheduleEditor: ScheduleEditor
    private val editors: EnumMap<ScheduleType, ScheduleEditor> = EnumMap(ScheduleType::class.java)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogArgs = Globals.getData("ScheduleDialog.Args") as ScheduleDialogArgs

        initializeComponent()
        setEditableLanguage(dialogArgs.editPermit.canEdit)

        return dialog
    }

    @SuppressLint("InflateParams")
    private fun initializeComponent(){
        val inflater: LayoutInflater = requireActivity().layoutInflater

        builder = AlertDialog.Builder(activity, R.style.Style_CloudBackup_Dialog)
        view = inflater.inflate(R.layout.dialog_schedule, null, false)

        skipEditText = view.findViewById(R.id.dialog_schedule_skipEditText)
        intervalUnitSpinner = view.findViewById(R.id.dialog_schedule_intervalUnitSpinner)
        intervalAdapter = ArrayAdapter<SpinnerItem>((context)!!, android.R.layout.simple_list_item_1, mutableListOf())
        intervalUnitSpinner.adapter = intervalAdapter
        timeOfDayButton = view.findViewById(R.id.dialog_schedule_changeButton)
        scheduleEditorLayout = view.findViewById(R.id.dialog_schedule_scheduleEditLinearLayout)
        scheduleTimeOfDayEditText = view.findViewById(R.id.dialog_schedule_timeOfDayEditText)

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
                onLanguagePackChange()
            }
        })

        intervalUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val selectedItem = intervalUnitSpinner.selectedItem as SpinnerItem
                setUnit(ScheduleType.valueOf(selectedItem.id))
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        timeOfDayButton.setOnClickListener {
            setTimeOfTheDay()
        }
        loadAllViews()

        val dialog = builder.create()
        dialog.setOnShowListener {
            okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            setLanguage(dialogArgs.editPermit.canEdit)
            setEditable(dialogArgs.editPermit.canEdit)
            setInput()
        }

        this.dialog = dialog
    }

    private fun loadAllViews() {
        if (dialogArgs.schedule.getReference().type === ScheduleType.Daily) {
            editors[ScheduleType.Daily] = DailyEditor(dialogArgs.schedule.getReference() as DailySchedule)
        }else{
            editors[ScheduleType.Daily] = DailyEditor(DailySchedule())
        }

        if (dialogArgs.schedule.getReference().type === ScheduleType.Weekly) {
            editors[ScheduleType.Weekly] = WeeklyEditorLayout((context)!!, dialogArgs.schedule.getReference() as WeeklySchedule)
        }else{
            editors[ScheduleType.Weekly] = WeeklyEditorLayout((context)!!, WeeklySchedule())
        }

        if (dialogArgs.schedule.getReference().type == ScheduleType.Monthly) {
            editors[ScheduleType.Monthly] = MonthlyEditorLayout((context)!!, dialogArgs.schedule.getReference() as MonthlySchedule)
        }else{
            editors[ScheduleType.Monthly] = MonthlyEditorLayout((context)!!, MonthlySchedule())
        }

        if (dialogArgs.schedule.getReference().type == ScheduleType.Yearly){
            editors[ScheduleType.Yearly] = YearlyEditorLayout((context)!!, dialogArgs.schedule.getReference() as YearlySchedule)
        } else{
            editors[ScheduleType.Yearly] = YearlyEditorLayout((context)!!, YearlySchedule())
        }
    }

    private fun setInput() {
        skipEditText.setText(java.lang.String.valueOf(dialogArgs.schedule.getReference().onceEvery))

        ViewUtilities.selectTranslatedSpinnerItem(intervalUnitSpinner, dialogArgs.schedule.getReference().type.toString())
        setTimeOfDayText(dialogArgs.schedule.getReference().timeOfDay.hour.toInt(), dialogArgs.schedule.getReference().timeOfDay.minute.toInt())
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage

        intervalAdapter.clear()
        intervalAdapter.add(SpinnerItem(ScheduleType.Daily.toString(), languagePack.getTranslation("scheduleDialog.typeSpinner.daily")))
        intervalAdapter.add(SpinnerItem(ScheduleType.Weekly.toString(), languagePack.getTranslation("scheduleDialog.typeSpinner.weekly")))
        intervalAdapter.add(SpinnerItem(ScheduleType.Monthly.toString(), languagePack.getTranslation("scheduleDialog.typeSpinner.monthly")))
        intervalAdapter.add(SpinnerItem(ScheduleType.Yearly.toString(), languagePack.getTranslation("scheduleDialog.typeSpinner.yearly")))
        intervalAdapter.notifyDataSetChanged()

        dialog.findViewById<TextView>(R.id.dialog_schedule_intervalTextView).text = languagePack.getTranslation("scheduleDialog.intervalTextView")
        dialog.findViewById<TextView>(R.id.dialog_schedule_timeOfDayTextView).text = languagePack.getTranslation("scheduleDialog.timeOfDayTextView")
        dialog.findViewById<TextView>(R.id.dialog_schedule_changeButton).text = languagePack.getTranslation("scheduleDialog.changeButton")

        negativeButton.text = languagePack.getTranslation("scheduleDialog.cancelButton")
        okButton.text = languagePack.getTranslation("scheduleDialog.okButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = dialogArgs.languagePermit.currentLanguage
        dialog.setTitle(LanguageUtilities.translateEditable("scheduleDialog.title", editable, languagePack))
    }

    private fun setEditable(editable: Boolean) {
        skipEditText.isEnabled = editable
        intervalUnitSpinner.isEnabled = editable
        timeOfDayButton.isEnabled = editable
        negativeButton.visibility = if (editable) View.VISIBLE else View.INVISIBLE

        if(this::scheduleEditor.isInitialized) scheduleEditor.setEditable(editable)
    }

    private fun setUnit(unit: ScheduleType) {
        scheduleEditorLayout.visibility = View.INVISIBLE
        if (this::scheduleEditor.isInitialized  && scheduleEditor.getView() != null) {
            scheduleEditorLayout.removeView(scheduleEditor.getView())
        }

        scheduleEditor = editors[unit]!!
        if (scheduleEditor.getView() != null) scheduleEditorLayout.addView(scheduleEditor.getView())

        scheduleEditorLayout.visibility = View.VISIBLE

        val languagePack = dialogArgs.languagePermit.currentLanguage
        scheduleEditor.setLanguage(languagePack, dialogArgs.editPermit.canEdit)
        scheduleEditor.setInput()
        scheduleEditor.setEditable(dialogArgs.editPermit.canEdit)
    }

    private fun onLanguagePackChange(){
        setLanguage(dialogArgs.editPermit.canEdit)

        if (this::scheduleEditor.isInitialized  && scheduleEditor.getView() != null) {
            scheduleEditor.setLanguage(dialogArgs.languagePermit.currentLanguage, dialogArgs.editPermit.canEdit)
        }
    }

    override fun onResume() {
        super.onResume()

        val params: WindowManager.LayoutParams = getDialog()?.window?.attributes!!
        params.width = 680
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT

        requireDialog().window?.attributes = params
    }

    override fun onCancel(dialog: DialogInterface) {
        dialogArgs.closeEvent.onInvoke()
    }

    private fun setTimeOfTheDay() {
        val values: IntArray = parseTimeString(scheduleTimeOfDayEditText.text.toString())
        val timePicker = TimePickerDialog(activity, R.style.Style_CloudBackup_Dialog, { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
            setTimeOfDayText(selectedHour, selectedMinute)}, values[0], values[1], true
        )

        val languagePack = dialogArgs.languagePermit.currentLanguage
        timePicker.setTitle(languagePack.getTranslation("scheduleDialog.selectTime"))
        timePicker.show()
    }

    private fun setTimeOfDayText(hour: Int, minute: Int) {
        val formatter = DecimalFormat("00")
        val text = "${formatter.format(hour.toLong())}:${formatter.format(minute.toLong())}"

        scheduleTimeOfDayEditText.setText(text)
    }

    private fun parseTimeString(string: String): IntArray {
        val values = string.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return intArrayOf(values[0].toInt(), values[1].toInt())
    }

    private fun save() {
        if (!dialogArgs.editPermit.canEdit) return

        val schedule: Schedule = scheduleEditor.getSchedule()

        schedule.invokeWithSingleEventTrigger {
            scheduleEditor.save()
            schedule.onceEvery = skipEditText.text.toString().toInt()

            val values = parseTimeString(scheduleTimeOfDayEditText.text.toString())
            schedule.timeOfDay = TimeOfDay(values[0].toByte(), values[1].toByte())
        }

        dialogArgs.schedule.setReference(schedule)
    }
}
