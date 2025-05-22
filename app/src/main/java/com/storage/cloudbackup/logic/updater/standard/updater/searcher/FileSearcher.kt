package com.storage.cloudbackup.logic.updater.standard.updater.searcher

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.updater.standard.updater.Output

interface FileSearcher {
    fun getFiles(scheme: Scheme): Output<List<LocalFile>>
}