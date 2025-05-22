package com.storage.cloudbackup.logic.model.item.mobile.data.usage

import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject
import java.time.LocalDateTime

class MobileDataUsage : ObservableObject() {
    var bytesUsage: Long = 0
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var lastUpdated: LocalDateTime = LocalDateTime.now()
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }
}