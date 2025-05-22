package com.storage.cloudbackup.logic.cloud.provider.model.upload

import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider

interface UploadProgress {
    fun onFileBeginUpload(currentFileUploadedBytesProvider: GenericProvider<BytesSent>)
    fun onFileEndUpload(successful: Boolean, message: String?)
}