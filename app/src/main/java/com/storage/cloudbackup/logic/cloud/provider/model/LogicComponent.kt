package com.storage.cloudbackup.logic.cloud.provider.model

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.cloud.provider.model.upload.UploadProgress
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

interface LogicComponent {
    val info: CloudProviderInfo

    val isOpenListener: MutableList<ArgsEvent<Boolean>>
    val isOpen: Boolean

    fun getAllFilesFromDirectory(directory: String): List<File>
    fun uploadFile(file: LocalFile, directory: String, uploadProgress: UploadProgress)

    fun load()
}