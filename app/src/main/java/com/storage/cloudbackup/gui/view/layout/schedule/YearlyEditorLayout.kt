package com.storage.cloudbackup.gui.view.layout.schedule

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.view.SpinnerItem
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.dialog.schedule.variant.ScheduleEditor
import com.storage.cloudbackup.logic.model.item.schedule.Month
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class YearlyEditorLayout(context: Context, private var schedule: YearlySchedule) : LinearLayout (context), ScheduleEditor
{
    private lateinit var dayOfYearEditText: EditText
    private lateinit var monthSpinner: Spinner
    private lateinit var monthAdapter: ArrayAdapter<SpinnerItem>
    private lateinit var daySpinner: Spinner
    private lateinit var dayAdapter: ArrayAdapter<String?>

    private var calls = 0

    init {
        initializeComponent()
    }

    private fun initializeComponent() {
        View.inflate(context, R.layout.layout_schedule_yearly, this)
        dayOfYearEditText = findViewById(R.id.layout_schedule_yearly_dayOfYearEditText)

        monthSpinner = findViewById(R.id.layout_schedule_yearly_monthSpinner)
        monthAdapter = ArrayAdapter<SpinnerItem>(context, android.R.layout.simple_list_item_1, mutableListOf())
        monthSpinner.adapter = monthAdapter

        daySpinner = findViewById(R.id.layout_schedule_yearly_daySpinner)
        dayAdapter = ArrayAdapter<String?>(context, android.R.layout.simple_list_item_1, mutableListOf())
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = dayAdapter

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                setDaysInMonth()
                setDayOfYearText()

                if (calls == 0) {
                    ViewUtilities.selectSpinnerItem(daySpinner, schedule.monthDay.toString())
                    calls++
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                setDayOfYearText()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    override fun setLanguage(languagePack: LanguagePack, editable: Boolean) {
        monthAdapter.clear()
        monthAdapter.add(SpinnerItem(Month.January.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.january")))
        monthAdapter.add(SpinnerItem(Month.February.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.february")))
        monthAdapter.add(SpinnerItem(Month.March.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.march")))
        monthAdapter.add(SpinnerItem(Month.April.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.april")))
        monthAdapter.add(SpinnerItem(Month.May.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.may")))
        monthAdapter.add(SpinnerItem(Month.June.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.june")))
        monthAdapter.add(SpinnerItem(Month.July.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.july")))
        monthAdapter.add(SpinnerItem(Month.August.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.august")))
        monthAdapter.add(SpinnerItem(Month.September.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.september")))
        monthAdapter.add(SpinnerItem(Month.October.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.october")))
        monthAdapter.add(SpinnerItem(Month.November.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.november")))
        monthAdapter.add(SpinnerItem(Month.December.toString(), languagePack.getTranslation("scheduleDialog.yearly.monthSpinner.december")))
        monthAdapter.notifyDataSetChanged()

        findViewById<TextView>(R.id.layout_schedule_yearly_dayOfTheYearTextView).text = languagePack.getTranslation("scheduleDialog.yearly.dayOfTheYearTextView")
    }

    override fun setInput() {
        setMonthInput()
        setDaysInMonth()
        setDayInput()
    }

    private fun setMonthInput(){
        val category= schedule.month.toString()
        ViewUtilities.selectTranslatedSpinnerItem(monthSpinner, category)
    }

    private fun setDaysInMonth() {
        val selectedItem = monthSpinner.selectedItem as SpinnerItem
        val days = getDaysInAMonth(selectedItem.id)

        ViewUtilities.selectSpinnerItem(daySpinner, "1")
        dayAdapter.clear()
        for (i in 0 until days) {
            dayAdapter.add((i + 1).toString())
        }

        dayAdapter.notifyDataSetChanged()
    }

    private fun setDayInput(){
        ViewUtilities.selectSpinnerItem(daySpinner, schedule.monthDay.toString())
    }

    private fun getDaysInAMonth(month: String): Int {
        return Month.valueOf(month).getAmountOfDays(2025)
    }

    private fun setDayOfYearText() {
        val text = "${monthSpinner.selectedItem} ${daySpinner.selectedItem}"
        dayOfYearEditText.setText(text)
    }

    override fun setEditable(editable: Boolean) {
        monthSpinner.isEnabled = editable
        daySpinner.isEnabled = editable
    }

    override fun save() {
        val selectedItem = monthSpinner.selectedItem as SpinnerItem
        schedule.month = Month.valueOf(selectedItem.id)
        schedule.monthDay = daySpinner.selectedItem.toString().toByte()
    }

    override fun getView(): View {
        return this
    }

    override fun getSchedule(): Schedule {
        val schedule = YearlySchedule()
        val selectedItem = monthSpinner.selectedItem as SpinnerItem
        schedule.month = Month.valueOf(selectedItem.id)
        schedule.monthDay = daySpinner.selectedItem.toString().toByte()
        return schedule
    }
}