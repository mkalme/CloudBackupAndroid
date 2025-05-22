package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.comparison

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile

interface FileComparison {
    fun compare(a: LocalFile, b: File): Boolean
}