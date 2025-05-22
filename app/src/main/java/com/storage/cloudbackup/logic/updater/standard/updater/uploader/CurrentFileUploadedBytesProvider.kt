package com.storage.cloudbackup.logic.updater.standard.updater.uploader

import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider

class CurrentFileUploadedBytesProvider : GenericProvider<BytesSent> {
    var provider: GenericProvider<BytesSent>? = null

    override fun provide(): BytesSent {
        return provider?.provide() ?: BytesSent(0u, 0u)
    }
}