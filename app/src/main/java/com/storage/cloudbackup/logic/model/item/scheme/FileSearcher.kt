package com.storage.cloudbackup.logic.model.item.scheme

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject

class FileSearcher : ObservableObject() {
    var fileNameFilter: String = "*"
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var minFileSize: DataAmount = DataAmount(DataUnit.B, 0u)
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var maxFileSize: DataAmount = DataAmount(DataUnit.GB, 999u)
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    override fun equals(other: Any?): Boolean {
        if(other == null || other !is FileSearcher) return false

        return fileNameFilter == other.fileNameFilter &&
                minFileSize == other.minFileSize &&
                maxFileSize == other.maxFileSize
    }

    fun clone(): FileSearcher {
        val output = FileSearcher()
        output.fileNameFilter = fileNameFilter
        output.minFileSize = minFileSize.clone()
        output.maxFileSize = maxFileSize

        return output
    }

    override fun hashCode(): Int {
        var result = fileNameFilter.hashCode()
        result = 31 * result + minFileSize.hashCode()
        result = 31 * result + maxFileSize.hashCode()
        return result
    }
}