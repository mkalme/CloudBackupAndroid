package com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.settings

import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class Settings : ObservableObject() {
    var accessKey: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) {
                invokeListener()
                invokeCredentialsChangedListener()
            }
        }

    var secretKey: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) {
                invokeListener()
                invokeCredentialsChangedListener()
            }
        }

    var bucket: String = ""
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var rootFolder: String = "Phone"
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    val credentialsChangedListener: MutableList<EmptyEvent> = mutableListOf()

    private fun invokeCredentialsChangedListener(){
        credentialsChangedListener.forEach {
            it.onInvoke()
        }
    }
}