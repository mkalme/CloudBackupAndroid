package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile

interface Filter {
    fun filter(file: LocalFile): Boolean
}