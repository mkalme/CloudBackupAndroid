package com.storage.cloudbackup.logic.shared.utilities.utilities

import android.content.Context
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import com.storage.cloudbackup.logic.scheduler.alarm.scheduler.StandardAlarmScheduler
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.UpdatePlanContainerTimeFinder
import java.time.LocalDateTime

object AlarmUtilities {
    fun setAlarm(updatePlanContainer: UpdatePlanContainer, context: Context){
        val time = UpdatePlanContainerTimeFinder().getNearestTime(updatePlanContainer, LocalDateTime.now(), FindType.After)
        if(time == LocalDateTime.MAX) return

        StandardAlarmScheduler(context).scheduleAt(time)
    }

    fun cancelAlarm(context: Context){
        StandardAlarmScheduler(context).cancel()
    }
}