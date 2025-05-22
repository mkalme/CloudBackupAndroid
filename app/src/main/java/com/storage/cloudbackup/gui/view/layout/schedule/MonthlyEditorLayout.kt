package com.storage.cloudbackup.gui.view.layout.schedule

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.dialog.schedule.variant.ScheduleEditor
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class MonthlyEditorLayout(context: Context, val schedule: MonthlySchedule) : LinearLayout(context),
    ScheduleEditor {
    private lateinit var daySpinner: Spinner

    init {
        initializeComponent()
    }

    private fun initializeComponent() {
        View.inflate(context, R.layout.layout_schedule_monthly, this)

        daySpinner = findViewById(R.id.layout_schedule_monthly_daySpinner)

        val adapter = ArrayAdapter<String?>(context, android.R.layout.simple_list_item_1, mutableListOf())
        for (i in 1 until 32) {
            adapter.add(i.toString())
        }

        daySpinner.adapter = adapter
    }

    override fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        findViewById<TextView>(R.id.layout_schedule_monthly_dayOfTheMonthTextView).text = languagePack.getTranslation("scheduleDialog.monthly.dayOfTheMonthTextView")
    }

    override fun setInput() {
        ViewUtilities.selectSpinnerItem(daySpinner, java.lang.String.valueOf(schedule.monthDay))
    }

    override fun setEditable(editable: Boolean) {
        daySpinner.isEnabled = editable
    }

    override fun save() {
        schedule.monthDay = daySpinner.selectedItem.toString().toByte()
    }

    override fun getView(): View {
        return this
    }

    override fun getSchedule(): Schedule {
        val schedule = MonthlySchedule()
        schedule.monthDay = daySpinner.selectedItem.toString().toByte()

        return schedule
    }
}