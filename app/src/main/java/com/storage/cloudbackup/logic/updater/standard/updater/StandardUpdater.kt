package com.storage.cloudbackup.logic.updater.standard.updater

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.upload.BytesSent
import com.storage.cloudbackup.logic.cloud.provider.model.upload.UploadProgress
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericProvider
import com.storage.cloudbackup.logic.updater.Updater
import com.storage.cloudbackup.logic.updater.event.args.FileCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStartArgs
import com.storage.cloudbackup.logic.updater.event.args.FileStopArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeCanceledArgs
import com.storage.cloudbackup.logic.updater.event.args.SchemeStopArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStartArgs
import com.storage.cloudbackup.logic.updater.event.args.UpdateStopArgs
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.FileSearcher
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.standard.StandardFileSearcher
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.validator.FileSearchStandardValidator
import com.storage.cloudbackup.logic.updater.standard.updater.searcher.validator.FileSearchValidator
import com.storage.cloudbackup.logic.updater.standard.updater.uploader.CurrentFileUploadedBytesProvider
import com.storage.cloudbackup.logic.updater.standard.updater.uploader.SchemeFileSourceOutput
import com.storage.cloudbackup.logic.updater.standard.updater.uploader.validator.FileUploadValidator
import com.storage.cloudbackup.logic.updater.standard.updater.uploader.validator.StandardFileUploadValidator

class StandardUpdater(mobileDataUsage: MobileDataUsage, settings: Settings, context: Context) : Updater {
    override val updateStartListener: MutableList<ArgsEvent<UpdateStartArgs>> = mutableListOf()
    override val updateStopListener: MutableList<ArgsEvent<UpdateStopArgs>> = mutableListOf()

    override val schemeFileSearchStartListener: MutableList<ArgsEvent<Scheme>> = mutableListOf()
    override val schemeFileSearchCanceledListener: MutableList<ArgsEvent<SchemeCanceledArgs>> = mutableListOf()
    override val schemeFileSearchStopListener: MutableList<ArgsEvent<SchemeStopArgs>> = mutableListOf()

    override val schemeUpdateStartListener: MutableList<ArgsEvent<Scheme>> = mutableListOf()
    override val schemeUpdateStopListener: MutableList<ArgsEvent<SchemeStopArgs>> = mutableListOf()

    override val fileUpdateStartListener: MutableList<ArgsEvent<FileStartArgs>> = mutableListOf()
    override val fileUpdateCanceledListener: MutableList<ArgsEvent<FileCanceledArgs>> = mutableListOf()
    override val fileUpdateStopListener: MutableList<ArgsEvent<FileStopArgs>> = mutableListOf()

    private val _byteProvider = CurrentFileUploadedBytesProvider()
    override val currentFileUploadedBytesProvider: GenericProvider<BytesSent>
        get() = _byteProvider

    private val fileSearcherValidator: FileSearchValidator = FileSearchStandardValidator(mobileDataUsage, settings, context)
    private val fileSearcher: FileSearcher = StandardFileSearcher()
    private val fileUploadValidator: FileUploadValidator = StandardFileUploadValidator(mobileDataUsage, settings, context)

    override fun updateSchemes(schemes: List<Scheme>, userInitiated: Boolean) {
        updateStartListener.forEach { it.onInvoke(UpdateStartArgs(userInitiated)) }

        val files = mutableListOf<SchemeFileSourceOutput>()
        schemes.forEach {
            val validOutput = fileSearcherValidator.validate(it)
            if(!validOutput.successful) {
                val args = SchemeCanceledArgs(it, validOutput.messages)
                schemeFileSearchCanceledListener.forEach { event -> event.onInvoke(args) }
                return@forEach
            }

            schemeFileSearchStartListener.forEach { event -> event.onInvoke(it) }

            val searchOutput = fileSearcher.getFiles(it)
            val args = SchemeStopArgs(it, searchOutput.successful, searchOutput.messages)
            schemeFileSearchStopListener.forEach { event -> event.onInvoke(args) }

            if(searchOutput.successful) files.add(SchemeFileSourceOutput(it, searchOutput.output))
        }

        if(files.size == 0 && schemes.isNotEmpty()) {
            val args = UpdateStopArgs(false, listOf(ErrorMessage.AllSchemesUnsuccessful.value))
            updateStopListener.forEach { it.onInvoke(args) }
            return
        }

        val output = updateFiles(files)

        val args = if(!output.successful){
            UpdateStopArgs(false, output.messages)
        }else{
            UpdateStopArgs(true)
        }

        updateStopListener.forEach { it.onInvoke(args) }
    }

    private fun updateFiles(schemeFiles: List<SchemeFileSourceOutput>) : Output<Any?>{
        val total = schemeFiles.sumOf { it.files.size }
        var current = 0

        schemeFiles.forEach {
            updateScheme(it, current, total)
            current += it.files.size
        }

        return Output(null, true)
    }

    private fun updateScheme(it: SchemeFileSourceOutput, current: Int, total: Int) {
        schemeUpdateStartListener.forEach { event -> event.onInvoke(it.scheme) }

        val args = try {
            for (i in 0 until it.files.size) {
                val validOutput = fileUploadValidator.validate(it.files[i])
                if (!validOutput.successful) {
                    val args = FileCanceledArgs(it.files[i], current + i, total, it.scheme, validOutput.messages)
                    fileUpdateCanceledListener.forEach { event -> event.onInvoke(args) }
                    continue
                }

                updateFile(it, i, current, total)
            }

            SchemeStopArgs(it.scheme, true)
        }catch (ex: Exception) {
            SchemeStopArgs(it.scheme, false, listOf(ex.message.toString()))
        }

        schemeUpdateStopListener.forEach { event -> event.onInvoke(args) }
    }

    private fun updateFile(schemeFiles: SchemeFileSourceOutput, index: Int, current: Int, total: Int){
        val uploadProgress = object: UploadProgress {
            override fun onFileBeginUpload(currentFileUploadedBytesProvider: GenericProvider<BytesSent>) {
                val args = FileStartArgs(schemeFiles.files[index], index + current, total, schemeFiles.scheme)

                _byteProvider.provider = currentFileUploadedBytesProvider
                fileUpdateStartListener.forEach { event -> event.onInvoke(args) }
            }

            override fun onFileEndUpload(successful: Boolean, message: String?) {
                val messages: List<String> = if(message == null) listOf() else listOf(message)

                val args = FileStopArgs(schemeFiles.files[index], index + current, total, schemeFiles.scheme, successful, messages)
                fileUpdateStopListener.forEach { event -> event.onInvoke(args) }
            }
        }

        val cloudProvider = schemeFiles.scheme.owner?.cloudProvider!!
        cloudProvider.logicComponent.uploadFile(schemeFiles.files[index], schemeFiles.scheme.driveFolder, uploadProgress)
    }
}