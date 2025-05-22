package com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.updater.standard.updater.ErrorMessage
import com.storage.cloudbackup.logic.updater.standard.updater.Output
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.FileSearcher
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.comparison.StandardFileComparison
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.filter.StandardFilter

class StandardFileSearcher : FileSearcher {
    override fun getFiles(scheme: Scheme): Output<List<LocalFile>> {
        if(scheme.owner == null) return Output(listOf(), false, listOf(ErrorMessage.Other.value))
        if(scheme.owner!!.cloudProvider == null) return Output(listOf(), false, listOf(ErrorMessage.Other.value))

        return try {
            val localFiles = getFilteredFilesFromScheme(scheme)
            if(!localFiles.successful){
                return Output(listOf(), false, localFiles.messages)
            }

            val cloudFiles = scheme.owner!!.cloudProvider!!.logicComponent.getAllFilesFromDirectory(scheme.driveFolder)
            compareLocalFilesToCloudFiles(scheme, localFiles.output, cloudFiles)

            return Output(localFiles.output, true)
        }catch (ex: Exception) {
            Output(listOf(), false, listOf(ex.message.toString()))
        }
    }

    private fun getFilteredFilesFromScheme(scheme: Scheme): Output<MutableList<LocalFile>> {
        val folder = scheme.owner?.folder!!
        val directory = java.io.File(folder)
        if(!directory.exists()) {
            return Output(mutableListOf(), false, listOf(ErrorMessage.DirectoryDoesNotExist.value))
        }

        val filter = StandardFilter(scheme.fileFilter)

        val files = mutableListOf<LocalFile>()
        directory.walk().forEach {
            if(it.isDirectory) return@forEach

            val name = it.absolutePath.substring(folder.length + 1)
            val size = it.length().toULong()

            val file = LocalFile(name, it.absolutePath, size)
            if(!filter.filter(file)) return@forEach

            files.add(file)
        }

        return Output(files, true)
    }

    private fun compareLocalFilesToCloudFiles(scheme: Scheme, localFiles: MutableList<LocalFile>, cloudFiles: List<File>){
        val fileComparison = StandardFileComparison(scheme.useFileSizeForComparison)
        val cloudFileMap = cloudFiles.associateBy { it.name }

        val localFileIterator = localFiles.iterator()
        while (localFileIterator.hasNext()) {
            val it = localFileIterator.next()
            if(!cloudFileMap.contains(it.name)) continue
            if(!fileComparison.compare(it, cloudFileMap[it.name]!!)) continue

            localFileIterator.remove()
        }
    }
}