package com.storage.cloudbackup.logic.model.item.history

import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject
import java.time.LocalDateTime
import java.util.UUID

class SchemeHistory(val id: UUID) : ObservableObject() {
    var lastUpdated: LocalDateTime = LocalDateTime.MIN
        set(value){
            val equals = field.isEqual(value)
            field = value
            if(!equals) invokeListener()
        }

    val updateAttempts: ObservableMutableList<UpdateAttempt> = ObservableMutableList.emptyList()

    private val event = object : EmptyEvent {
        override fun onInvoke() {
            invokeListener()
        }
    }

    init {
        updateAttempts.collectionChangedListener.add(object: CollectionChangedEvent<UpdateAttempt> {
            override fun onInvoke(removed: Collection<UpdateAttempt?>, added: Collection<UpdateAttempt?>) {
                removed.forEach {
                    it?.schemeId = null
                    it?.propertyChangedListener?.remove(event)
                }

                added.forEach {
                    it?.schemeId = id
                    it?.propertyChangedListener?.add(event)
                }

                invokeListener()
            }
        })
    }
}