package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.WriteChannel
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.storage.cloudbackup.R
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProviderInfo
import com.storage.cloudbackup.logic.cloud.provider.model.LogicComponent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.cloud.provider.model.upload.UploadProgress
import com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.settings.SettingsResource
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.CloudUploadUtilities
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

class CloudLogicComponent(private val context: Context) : LogicComponent {
    private val _info = CloudProviderInfo("google.storage", "Google Cloud Storage", R.mipmap.cloud_provider_google_storage)
    override val info: CloudProviderInfo
        get() = _info

    override val isOpenListener: MutableList<ArgsEvent<Boolean>> = mutableListOf()
    override val isOpen: Boolean
        get() = storage != null && settingsResource?.settings?.bucket?.isNotEmpty() ?: false

    var settingsResource: SettingsResource? = null
        private set

    private var storage: Storage? = null

    init {
        invokeUpdateListener(false)
    }

    override fun load() {
        settingsResource = SettingsResource(context)

        authenticate()

        settingsResource!!.settings.propertyChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                invokeUpdateListener(isOpen)
            }
        })

        settingsResource!!.settings.credentialsChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                authenticate()
            }
        })
    }

    private fun authenticate(){
        invokeUpdateListener(false)

        Thread {
            if(settingsResource == null) return@Thread

            if(settingsResource!!.settings.accountCredentials.isNotEmpty()){
                try {
                    val credentials = GoogleCredentials.fromStream(ByteArrayInputStream(settingsResource!!.settings.accountCredentials.toByteArray()))
                    storage = StorageOptions.newBuilder().setCredentials(credentials).build().service

                    invokeUpdateListener(true)
                }catch(ex: Exception){
                    storage = null
                    invokeUpdateListener(false)
                }
            }
        }.start()
    }

    override fun getAllFilesFromDirectory(directory: String): List<File> {
        if(storage == null || settingsResource == null) return mutableListOf()

        val rootDirectory = CloudUploadUtilities.createFullDirectoryPath(settingsResource?.settings?.rootFolder, directory)
        val directorySubstring = if(rootDirectory.isNotEmpty()) "${rootDirectory}/" else ""

        val blobs = storage?.list(settingsResource!!.settings.bucket, Storage.BlobListOption.prefix(rootDirectory))

        val output = mutableListOf<File>()
        for (blob in blobs?.iterateAll()!!) {
            if(blob.isDirectory || blob.name == directorySubstring) continue
            output.add(File(blob.name.substring(directorySubstring.length), blob.size.toULong()))
        }

        return output
    }

    override fun uploadFile(file: LocalFile, directory: String, uploadProgress: UploadProgress) {
        if(storage == null || settingsResource == null) return

        var bytesUploaded: ULong = 0u
        var lastBytesUploaded: ULong = 0u
        var maxBytes: ULong = 0u

        val buffer = ByteArray(1024 * 256)

        val byteProvider = object: GenericProvider<BytesSent> {
            override fun provide(): BytesSent {
                return BytesSent(lastBytesUploaded, maxBytes)
            }
        }

        uploadProgress.onFileBeginUpload(byteProvider)

        maxBytes = file.size

        val path = CloudUploadUtilities.createFullFilePath(settingsResource?.settings?.rootFolder, directory, file.name)
        val blobId = BlobId.of(settingsResource!!.settings.bucket, path)
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        val writer: WriteChannel = storage?.writer(blobInfo)!!

        Files.newInputStream(Paths.get(file.fullPath)).use { inputStream ->
            var limit: Int
            while (inputStream.read(buffer).also { limit = it } >= 0) {
                writer.write(ByteBuffer.wrap(buffer, 0, limit))
                lastBytesUploaded = bytesUploaded
                bytesUploaded += limit.toULong()
            }
        }

        writer.close()

        uploadProgress.onFileEndUpload(true, null)
    }

    private fun invokeUpdateListener(isOpen: Boolean){
        isOpenListener.forEach {
            it.onInvoke(isOpen)
        }
    }
}