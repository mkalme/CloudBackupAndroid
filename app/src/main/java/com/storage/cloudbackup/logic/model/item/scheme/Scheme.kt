package com.storage.cloudbackup.logic.model.item.scheme

import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject
import java.util.UUID

class Scheme(val id: UUID = UUID.randomUUID()) : ObservableObject() {
    var name: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var driveFolder: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    private var _fileFilter: FileSearcher
    var fileFilter: FileSearcher
        get() = _fileFilter
        set(value){
            if(_fileFilter === value) return

            _fileFilter.propertyChangedListener.remove(event)
            _fileFilter = value
            _fileFilter.propertyChangedListener.add(event)

            invokeListener()
        }

    var networkType: NetworkType = NetworkType.Wifi
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var useFileSizeForComparison: Boolean = false
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var history: SchemeHistory = SchemeHistory(id)

    var owner: UpdatePlan? = null

    val schedules: ObservableMutableList<Schedule> = ObservableMutableList.emptyList()

    private val event = object : EmptyEvent {
        override fun onInvoke() {
            invokeListener()
        }
    }

    init {
        _fileFilter = FileSearcher()
        _fileFilter.propertyChangedListener.add(event)
        schedules.collectionChangedListener.add(object: CollectionChangedEvent<Schedule> {
            override fun onInvoke(removed: Collection<Schedule?>, added: Collection<Schedule?>) {
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

    fun clone(): Scheme {
        val output = Scheme(id)
        output.name = name
        output.driveFolder = driveFolder
        output.fileFilter = fileFilter.clone()
        output.networkType = networkType
        output.useFileSizeForComparison = useFileSizeForComparison
        output.history = history
        output.owner = owner

        schedules.forEach{
            output.schedules.add(it.clone())
        }

        return output
    }
}