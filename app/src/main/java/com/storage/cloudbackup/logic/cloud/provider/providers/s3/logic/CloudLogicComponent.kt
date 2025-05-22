package com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsV2Request
import com.amazonaws.services.s3.model.ListObjectsV2Result
import com.amazonaws.services.s3.model.PutObjectRequest
import com.storage.cloudbackup.R
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProviderInfo
import com.storage.cloudbackup.logic.cloud.provider.model.LogicComponent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.cloud.provider.model.upload.UploadProgress
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.settings.SettingsResource
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.CloudUploadUtilities
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider

class CloudLogicComponent(private val context: Context) : LogicComponent {
    private val _info = CloudProviderInfo("amazon.aws.s3", "Amazon AWS S3", R.mipmap.cloud_provider_s3)
    override val info: CloudProviderInfo
        get() = _info

    override val isOpenListener: MutableList<ArgsEvent<Boolean>> = mutableListOf()
    override val isOpen: Boolean
        get() = client != null && settingsResource?.settings?.bucket?.isNotEmpty() ?: false

    var settingsResource: SettingsResource? = null
        private set

    private var client: AmazonS3? = null

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

            if(settingsResource!!.settings.accessKey.isNotEmpty() && settingsResource!!.settings.secretKey.isNotEmpty()){
                try {
                    val awsCredentials = BasicAWSCredentials(settingsResource!!.settings.accessKey, settingsResource!!.settings.secretKey)
                    client = AmazonS3Client(awsCredentials)

                    invokeUpdateListener(true)
                }catch(ex: Exception){
                    client = null
                    invokeUpdateListener(false)
                }
            }
        }.start()
    }

    override fun getAllFilesFromDirectory(directory: String): List<File> {
        if(client == null || settingsResource == null) return mutableListOf()

        val rootDirectory = CloudUploadUtilities.createFullDirectoryPath(settingsResource?.settings?.rootFolder, directory)
        val directorySubstring = if(rootDirectory.isNotEmpty()) "${rootDirectory}/" else ""

        val request = ListObjectsV2Request().withBucketName(settingsResource!!.settings.bucket).withPrefix(directorySubstring)
        var result: ListObjectsV2Result

        val output = mutableListOf<File>()

        do {
            result = client?.listObjectsV2(request)!!

            for (s3Object in result.objectSummaries) {
                if (s3Object.key == directorySubstring) continue

                output.add(File(s3Object.key.substring(directorySubstring.length), s3Object.size.toULong()))
            }

            request.continuationToken = result.nextContinuationToken
        } while (result.isTruncated)

        return output
    }

    override fun uploadFile(file: LocalFile, directory: String, uploadProgress: UploadProgress) {
        if(client == null || settingsResource == null) return

        var bytesTransferred: ULong = 0u
        var maxBytes: ULong = 0u

        val byteProvider = object: GenericProvider<BytesSent> {
            override fun provide(): BytesSent {
                return BytesSent(bytesTransferred, maxBytes)
            }
        }

        uploadProgress.onFileBeginUpload(byteProvider)

        maxBytes = file.size

        val path = CloudUploadUtilities.createFullFilePath(settingsResource?.settings?.rootFolder, directory, file.name)
        val request = PutObjectRequest(settingsResource!!.settings.bucket, path, java.io.File(file.fullPath))
        request.setGeneralProgressListener{
            bytesTransferred = it.bytesTransferred.toULong()
        }

        client?.putObject(request)

        uploadProgress.onFileEndUpload(true, null)
    }

    private fun invokeUpdateListener(isOpen: Boolean){
        isOpenListener.forEach{
            it.onInvoke(isOpen)
        }
    }
}