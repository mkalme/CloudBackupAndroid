package com.storage.cloudbackup.gui.view.adapter.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.dialog.schedule.ScheduleDialog
import com.storage.cloudbackup.gui.view.dialog.schedule.ScheduleDialogArgs
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.ScheduleType
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.Reference
import java.text.DecimalFormat

class ScheduleAdapter(val layoutArgs: ScheduleAdapterArgs) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private var dialogOpened = false
    lateinit var removedButtonClickListener: ArgsEvent<Reference<Schedule>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule: Reference<Schedule> = layoutArgs.scheduleReferences[position]
        schedule.setContentsChangedListener(object : EmptyEvent {
            override fun onInvoke() {
                loadListView(holder, schedule.getReference())
            }
        })

        schedule.getReference().propertyChangedListener.add(object : EmptyEvent {
            override fun onInvoke() {
                loadListView(holder, schedule.getReference())
            }
        })

        val closeEvent = object : EmptyEvent {
            override fun onInvoke() {
                dialogOpened = false
            }
        }

        loadListView(holder, schedule.getReference())
        holder.scheduleLinearLayout.setOnClickListener(View.OnClickListener {
            if (dialogOpened) return@OnClickListener
            dialogOpened = true

            val args = ScheduleDialogArgs(schedule, layoutArgs.editPermit, layoutArgs.languagePermit, closeEvent)
            Globals.setData("ScheduleDialog.Args", args)

            val dialog = ScheduleDialog()
            dialog.show((layoutArgs.context as FragmentActivity?)!!.supportFragmentManager, "")
        })

        layoutArgs.editPermit.editListener.add(object: EmptyEvent {
            override fun onInvoke() {
                holder.removeButton.isEnabled = layoutArgs.editPermit.canEdit
                holder.removeButton.visibility = if (layoutArgs.editPermit.canEdit) View.VISIBLE else View.INVISIBLE
            }
        })

        layoutArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(holder)
            }
        })

        holder.removeButton.isEnabled = layoutArgs.editPermit.canEdit
        holder.removeButton.visibility = if (layoutArgs.editPermit.canEdit) View.VISIBLE else View.INVISIBLE
        holder.removeButton.setOnClickListener {
            removedButtonClickListener.onInvoke(schedule)
        }

        setLanguage(holder)
    }

    private fun setLanguage(holder: ScheduleViewHolder){
        holder.removeButton.text = layoutArgs.languagePermit.currentLanguage.getTranslation("scheduleAdapter.removeButton")

    }

    private fun loadListView(holder: ScheduleViewHolder, schedule: Schedule) {
        var headerString = "Once a "
        var interval = ""
        var end = ""
        if (schedule.onceEvery > 1) {
            headerString = "Once every " + schedule.onceEvery + " "
            end = "s"
        }

        val formatter = DecimalFormat("00")
        var bottomText = formatter.format(schedule.timeOfDay.hour) + ":" + formatter.format(
            schedule.timeOfDay.minute
        )

        when (schedule.type) {
            ScheduleType.Daily -> {
                interval = "day"
            }
            ScheduleType.Weekly -> {
                interval = "week"
                bottomText += ", " + (schedule as WeeklySchedule).weekday.toString()
            }
            ScheduleType.Monthly -> {
                interval = "month"
                bottomText += ", on the " + (schedule as MonthlySchedule).monthDay.toString() + appendToNumber(schedule.monthDay.toInt())
            }
            ScheduleType.Yearly -> {
                interval = "year"
                bottomText += ", on " + (schedule as YearlySchedule).month.toString() + " the " + schedule.monthDay.toString() + appendToNumber(schedule.monthDay.toInt())
            }
        }

        val text = "${headerString}${interval}${end}"
        holder.scheduleIntervalEditText.text = text
        holder.scheduleTimeOfDayTextView.text = bottomText
    }

    private fun appendToNumber(n: Int) : String {
        val mod = n % 10

        if(mod == 1) return "st"
        if(mod == 2) return "nd"
        if(mod == 3) return "rd"
        return "th"
    }

    override fun getItemCount(): Int {
        return layoutArgs.scheduleReferences.size
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleLinearLayout: LinearLayout
        val scheduleIntervalEditText: TextView
        val scheduleTimeOfDayTextView: TextView
        val removeButton: Button

        init {
            scheduleLinearLayout = itemView.findViewById(R.id.adapter_schedule_layout)
            scheduleIntervalEditText = itemView.findViewById(R.id.adapter_schedule_intervalEditText)
            scheduleTimeOfDayTextView = itemView.findViewById(R.id.adapter_schedule_timeOfDayTextView)
            removeButton = itemView.findViewById(R.id.adapter_schedule_removeButton)

            ViewUtilities.setOnTouchListener(
                ViewUtilities.getAdapterColor(itemView.context),
                scheduleLinearLayout,
                itemView.findViewById(R.id.adapter_schedule_subLayout),
                itemView.findViewById(R.id.adapter_schedule_separatorView),
                scheduleIntervalEditText,
                scheduleTimeOfDayTextView
            )
        }
    }
}