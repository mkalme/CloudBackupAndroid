package com.storage.cloudbackup.logic.shared.utilities.resource

import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

abstract class JsonResourceObject : ResourceObject() {
    override fun load(inputStream: InputStream) {
        try {
            val json = inputStream.bufferedReader().use { it.readText() }
            load(JSONObject(json))
        }catch (ignored: Exception) {
            createDefault()
        }
    }

    override fun save(outputStream: OutputStream) {
        val json = save().toString()

        val writer = OutputStreamWriter(outputStream, Charsets.UTF_8)
        writer.write(json)
        writer.flush()
        writer.close()
    }

    abstract fun load(data: JSONObject)
    abstract fun save(): JSONObject
}