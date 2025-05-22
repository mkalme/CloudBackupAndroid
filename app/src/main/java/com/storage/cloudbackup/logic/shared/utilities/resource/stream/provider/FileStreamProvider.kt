package com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider

import android.content.Context
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class FileStreamProvider(private val filePath: String) : StreamProvider {
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    companion object {
        fun fromLocalFile(filePath: String, context: Context) : FileStreamProvider {
            return FileStreamProvider(File(context.filesDir, filePath).absolutePath)
        }
    }

    override fun isInputStreamAvailable(): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    override fun getInputStream(): InputStream {
        val file = File(filePath)

        val inputStream = file.inputStream()
        this.inputStream = inputStream

        return inputStream
    }

    override fun notifyInputStreamFinished() {
        inputStream?.close()
    }

    override fun getOutputStream(): OutputStream {
        val file = File(filePath)

        val outputStream = file.outputStream()
        this.outputStream = outputStream

        return outputStream
    }

    override fun notifyOutputStreamFinished() {
        outputStream?.close()
    }
}