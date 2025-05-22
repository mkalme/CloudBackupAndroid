package com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider

import java.io.InputStream
import java.io.OutputStream

interface StreamProvider {
    fun isInputStreamAvailable(): Boolean

    fun getInputStream(): InputStream
    fun notifyInputStreamFinished()

    fun getOutputStream(): OutputStream
    fun notifyOutputStreamFinished()
}