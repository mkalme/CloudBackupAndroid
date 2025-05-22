package com.storage.cloudbackup.logic.updater.standard.updater.uploader.validator

import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.updater.standard.updater.Output

interface FileUploadValidator {
    fun validate(file: LocalFile): Output<Any?>
}