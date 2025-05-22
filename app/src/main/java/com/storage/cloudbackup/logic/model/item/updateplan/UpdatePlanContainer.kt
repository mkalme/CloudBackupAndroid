package com.storage.cloudbackup.logic.model.item.updateplan

import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class UpdatePlanContainer : ObservableObject() {
    val updatePlans: ObservableMutableList<UpdatePlan> = ObservableMutableList.emptyList()

    private val event = object : EmptyEvent {
        override fun onInvoke() {
            invokeListener()
        }
    }

    init {
        val thisReference = this
        updatePlans.collectionChangedListener.add(object: CollectionChangedEvent<UpdatePlan> {
            override fun onInvoke(removed: Collection<UpdatePlan?>, added: Collection<UpdatePlan?>) {
                removed.forEach {
                    it?.propertyChangedListener?.remove(event)
                    it?.owner = null
                }

                added.forEach {
                    it?.propertyChangedListener?.add(event)
                    it?.owner = thisReference
                }

                invokeListener()
            }
        })
    }
}