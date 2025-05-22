package com.storage.cloudbackup.logic.cloud.provider.providers.demo.logic.settings

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class Settings : ObservableObject() {
    var rootFolder: String = "Phone"
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var simulatedUploadRate: DataAmount = DataAmount(DataUnit.MB, 5u)
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var simulatedLatencyMilliseconds: Int = 500
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var introduceRandomErrors: Boolean = false
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var trackUploadedFiles: Boolean = true
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    val simulatedUploadedFiles: ObservableMutableList<File> = ObservableMutableList.emptyList()

    init {
        simulatedUploadedFiles.collectionChangedListener.add(object: CollectionChangedEvent<File> {
            override fun onInvoke(removed: Collection<File?>, added: Collection<File?>) {
                invokeListener()
            }
        })
    }
}