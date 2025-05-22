package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.name.NameFilter
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.name.StandardNameFilter
import java.nio.file.Paths

class StandardFilter(var filter: FileSearcher) : Filter {
    private var nameFilter: NameFilter = StandardNameFilter(filter.fileNameFilter)

    override fun filter(file: LocalFile): Boolean {
        if(file.size < filter.minFileSize.getLongSize()) return false
        if(file.size >= filter.maxFileSize.getLongSize()) return false

        return nameFilter.filter(Paths.get(file.name).fileName.toString())
    }
}