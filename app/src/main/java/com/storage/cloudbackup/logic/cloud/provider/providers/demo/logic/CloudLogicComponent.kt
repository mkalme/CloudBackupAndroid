package com.storage.cloudbackup.logic.cloud.provider.providers.demo.logic

import android.content.Context
import com.storage.cloudbackup.R
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProviderInfo
import com.storage.cloudbackup.logic.cloud.provider.model.LogicComponent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.cloud.provider.model.upload.LocalFile
import com.storage.cloudbackup.logic.cloud.provider.model.upload.UploadProgress
import com.storage.cloudbackup.logic.cloud.provider.providers.demo.logic.settings.SettingsResource
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.CloudUploadUtilities
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class CloudLogicComponent(private val context: Context) : LogicComponent {
    private val _info = CloudProviderInfo("demo.storage", "Demo Cloud Storage", R.mipmap.cloud_provider_demo)
    override val info: CloudProviderInfo
        get() = _info

    override val isOpenListener: MutableList<ArgsEvent<Boolean>> = mutableListOf()
    private var _isOpen: Boolean = false
    override val isOpen: Boolean
        get() = _isOpen

    var settingsResource: SettingsResource? = null
        private set

    init {
        invokeUpdateListener(false)
    }

    override fun load() {
        settingsResource = SettingsResource(context)

        invokeUpdateListener(false)

        Thread {
            Thread.sleep(2000)
            _isOpen = true
            invokeUpdateListener(true)
        }.start()
    }

    override fun getAllFilesFromDirectory(directory: String): List<File> {
        Thread.sleep(settingsResource?.settings?.simulatedLatencyMilliseconds?.toLong() ?: 0)

        val rootDirectory = CloudUploadUtilities.createFullDirectoryPath(settingsResource?.settings?.rootFolder, directory)
        val directorySubstring = if(rootDirectory.isNotEmpty()) "${rootDirectory}/" else ""

        val output = mutableListOf<File>()
        for (file in settingsResource?.settings?.simulatedUploadedFiles!!) {
            if(!file.name.startsWith(directorySubstring)) continue

            output.add(File(file.name.substring(directorySubstring.length), file.size))
        }

        return output
    }

    override fun uploadFile(file: LocalFile, directory: String, uploadProgress: UploadProgress) {
        val path = CloudUploadUtilities.createFullFilePath(settingsResource?.settings?.rootFolder, directory, file.name)

        val bytesPerSecond = settingsResource?.settings?.simulatedUploadRate?.getLongSize()!!
        var sent = 0L

        val byteProvider = object: GenericProvider<BytesSent> {
            override fun provide(): BytesSent {
                return BytesSent(sent.toULong(), file.size)
            }
        }

        Thread.sleep(settingsResource?.settings?.simulatedLatencyMilliseconds?.toLong() ?: 0)

        uploadProgress.onFileBeginUpload(byteProvider)

        val sleepTime = 250
        while(sent < file.size.toLong()){
            var sleepFor = (file.size.toLong() - sent).toDouble() / bytesPerSecond.toDouble() * 1000
            sleepFor = min(sleepFor.toLong(), sleepTime.toLong()).toDouble()
            Thread.sleep(sleepFor.toLong())

            val send = ceil(bytesPerSecond.toDouble() * (sleepFor / 1000.0)).toLong()
            sent = min(sent + max(send, 1), file.size.toLong())
        }

        if(settingsResource?.settings?.trackUploadedFiles!!){
            settingsResource?.settings?.simulatedUploadedFiles?.add(File(path, file.size))
        }

        uploadProgress.onFileEndUpload(true, null)
    }

    private fun invokeUpdateListener(isOpen: Boolean){
        isOpenListener.forEach {
            it.onInvoke(isOpen)
        }
    }
}