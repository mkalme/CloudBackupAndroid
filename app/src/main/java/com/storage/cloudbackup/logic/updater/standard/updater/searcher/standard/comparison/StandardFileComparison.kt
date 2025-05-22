package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.comparison

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile

class StandardFileComparison(private var compareFileSizes: Boolean = false) : FileComparison {
    override fun compare(a: LocalFile, b: File): Boolean {
        if(a.name != b.name) return false
        if(!compareFileSizes) return false

        return a.size == b.size
    }
}