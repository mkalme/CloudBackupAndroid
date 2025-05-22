package com.storage.cloudbackup.logic.scheduler.updater

import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer

interface ScheduledUpdater {
    fun update(updatePlanContainer: UpdatePlanContainer)
}