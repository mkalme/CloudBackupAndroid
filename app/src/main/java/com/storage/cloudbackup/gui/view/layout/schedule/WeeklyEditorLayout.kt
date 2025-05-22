package com.storage.cloudbackup.gui.view.layout.schedule

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.view.SpinnerItem
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.dialog.schedule.variant.ScheduleEditor
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.WeekDay
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class WeeklyEditorLayout(context: Context, val schedule: WeeklySchedule) : LinearLayout(context),
    ScheduleEditor {
    private lateinit var weekDaySpinner: Spinner
    private lateinit var adapter: ArrayAdapter<SpinnerItem>

    init {
        initializeComponent()
    }

    private fun initializeComponent(){
        View.inflate(context, R.layout.layout_schedule_weekly, this)

        weekDaySpinner = findViewById(R.id.layout_schedule_weekly_weekDaySpinner)
        adapter = ArrayAdapter<SpinnerItem>(context, android.R.layout.simple_list_item_1, mutableListOf())
        weekDaySpinner.adapter = adapter
    }

    override fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        adapter.clear()
        adapter.add(SpinnerItem(WeekDay.Monday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.monday")))
        adapter.add(SpinnerItem(WeekDay.Tuesday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.tuesday")))
        adapter.add(SpinnerItem(WeekDay.Wednesday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.wednesday")))
        adapter.add(SpinnerItem(WeekDay.Thursday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.thursday")))
        adapter.add(SpinnerItem(WeekDay.Friday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.friday")))
        adapter.add(SpinnerItem(WeekDay.Saturday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.saturday")))
        adapter.add(SpinnerItem(WeekDay.Sunday.toString(), languagePack.getTranslation("scheduleDialog.weekly.weekDaySpinner.sunday")))
        adapter.notifyDataSetChanged()

        findViewById<TextView>(R.id.layout_schedule_weekly_dayOfTheWeekTextView).text = languagePack.getTranslation("scheduleDialog.weekly.dayOfTheWeekTextView")
    }

    override fun setInput(){
        ViewUtilities.selectTranslatedSpinnerItem(weekDaySpinner, schedule.weekday.toString())
    }

    override fun setEditable(editable: Boolean) {
        weekDaySpinner.isEnabled = editable
    }

    override fun save() {
        val selectedItem = weekDaySpinner.selectedItem as SpinnerItem
        schedule.weekday = WeekDay.valueOf(selectedItem.id)
    }

    override fun getView(): View {
        return this
    }

    override fun getSchedule(): Schedule {
        val schedule = WeeklySchedule()

        val selectedItem = weekDaySpinner.selectedItem as SpinnerItem
        schedule.weekday = WeekDay.valueOf(selectedItem.id)

        return schedule
    }
}