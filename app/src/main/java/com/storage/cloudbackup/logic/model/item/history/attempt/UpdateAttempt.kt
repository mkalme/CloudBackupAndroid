package com.storage.cloudbackup.logic.model.item.history.attempt

import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject
import java.time.LocalDateTime
import java.util.UUID

class UpdateAttempt : ObservableObject() {
    var begin: LocalDateTime = LocalDateTime.MIN
        set(value){
            val equals = field.isEqual(value)
            field = value
            if(!equals) invokeListener()
        }

    var finished: LocalDateTime = LocalDateTime.MIN
        set(value){
            val equals = field.isEqual(value)
            field = value
            if(!equals) invokeListener()
        }

    var result: AttemptResult = AttemptResult.Unfinished
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var localDirectory: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var driveDirectory: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var cloudProviderId: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var isAutomatic: Boolean = false
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    val filesUploaded: ObservableMutableList<String> = ObservableMutableList.emptyList()
    val unsuccessfulFilesUploaded: ObservableMutableList<String> = ObservableMutableList.emptyList()
    val schemeErrorMessages: ObservableMutableList<String> = ObservableMutableList.emptyList()

    var schemeId: UUID? = null

    init {
        filesUploaded.collectionChangedListener.add(object: CollectionChangedEvent<String> {
            override fun onInvoke(removed: Collection<String?>, added: Collection<String?>) {
                invokeListener()
            }
        })
        unsuccessfulFilesUploaded.collectionChangedListener.add(object: CollectionChangedEvent<String> {
            override fun onInvoke(removed: Collection<String?>, added: Collection<String?>) {
                invokeListener()
            }
        })
        schemeErrorMessages.collectionChangedListener.add(object: CollectionChangedEvent<String> {
            override fun onInvoke(removed: Collection<String?>, added: Collection<String?>) {
                invokeListener()
            }
        })
    }
}