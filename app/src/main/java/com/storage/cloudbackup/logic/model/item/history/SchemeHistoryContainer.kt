package com.storage.cloudbackup.logic.model.item.history

import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class SchemeHistoryContainer : ObservableObject() {
    val schemes: ObservableMutableList<SchemeHistory> = ObservableMutableList.emptyList()

    private val event = object : EmptyEvent {
        override fun onInvoke() {
            invokeListener()
        }
    }

    init {
        schemes.collectionChangedListener.add(object: CollectionChangedEvent<SchemeHistory> {
            override fun onInvoke(removed: Collection<SchemeHistory?>, added: Collection<SchemeHistory?>) {
                removed.forEach {
                    it?.propertyChangedListener?.remove(event)
                }

                added.forEach {
                    it?.propertyChangedListener?.add(event)
                }

                invokeListener()
            }
        })
    }

    fun getHistory(scheme: Scheme) : SchemeHistory? {
        return schemes.firstOrNull {
            it.id == scheme.id
        }
    }
}