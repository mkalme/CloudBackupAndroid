package com.storage.cloudbackup.logic.model.item.updateplan

import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class UpdatePlan : ObservableObject() {
    var name: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var folder: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    val schemes: ObservableMutableList<Scheme> = ObservableMutableList.emptyList()

    var cloudProvider: CloudProvider? = null
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var owner: UpdatePlanContainer? = null

    private val event = object : EmptyEvent {
        override fun onInvoke() {
            invokeListener()
        }
    }

    init {
        val thisReference = this

        schemes.collectionChangedListener.add(object: CollectionChangedEvent<Scheme> {
            override fun onInvoke(removed: Collection<Scheme?>, added: Collection<Scheme?>) {
                removed.forEach {
                    it?.owner = null
                    it?.propertyChangedListener?.remove(event)
                }

                added.forEach {
                    it?.owner = thisReference
                    it?.propertyChangedListener?.add(event)
                }

                invokeListener()
            }
        })
    }

    fun clone(): UpdatePlan {
        val output = UpdatePlan()
        output.name = name
        output.folder = folder
        output.owner = owner

        schemes.forEach {
            output.schemes.add(it.clone())
        }

        return output
    }
}