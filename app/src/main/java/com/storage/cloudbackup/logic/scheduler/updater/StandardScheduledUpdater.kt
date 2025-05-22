package com.storage.cloudbackup.logic.scheduler.updater

import android.util.Log
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.scheduler.finder.SchemeTimeFinder
import com.storage.cloudbackup.logic.updater.Updater
import java.time.LocalDateTime

class StandardScheduledUpdater(private val updater: Updater) : ScheduledUpdater {
    private val schemeTimeFinder: NearestUpdateTimeFinder<Scheme> = SchemeTimeFinder()
    private val updateTimeAheadMinutes = 10L

    override fun update(updatePlanContainer: UpdatePlanContainer) {
        val updateList = mutableListOf<Scheme>()

        do {
            updateList.clear()

            updatePlanContainer.updatePlans.forEach {
                it.schemes.forEach { scheme ->
                    run {
                        var update = true
                        if(!scheme.history.lastUpdated.isEqual(LocalDateTime.MIN)){
                            val currentTime = LocalDateTime.now().plusMinutes(updateTimeAheadMinutes)

                            val last = schemeTimeFinder.getNearestTime(scheme, currentTime, FindType.Before)
                            update = last.isAfter(scheme.history.lastUpdated)
                        }

                        if(update) updateList.add(scheme)
                    }
                }
            }

            if(updateList.size == 0) return

            updateList.forEach {
                updater.updateSchemes(listOf(it), false)
                Log.d("TAG1", "Updated: ${it.owner?.name} | ${it.name}")
            }
        }while (true)
    }
}