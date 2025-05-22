package com.storage.cloudbackup.logic.scheduler.finder

import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import java.time.LocalDateTime

class UpdatePlanContainerTimeFinder : NearestUpdateTimeFinder<UpdatePlanContainer> {
    private var schemeTimeFinder: NearestUpdateTimeFinder<Scheme> = SchemeTimeFinder()

    override fun getNearestTime(args: UpdatePlanContainer, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        var date = if(findType == FindType.After) LocalDateTime.MAX else LocalDateTime.MIN

        args.updatePlans.forEach {
            it.schemes.forEach { scheme ->
                val thisDate = schemeTimeFinder.getNearestTime(scheme, currentTime, findType)

                if(findType == FindType.After){
                    if(thisDate.isBefore(date)) date = thisDate
                }else{
                    if(thisDate.isAfter(date)) date = thisDate
                }
            }
        }

        return date
    }
}