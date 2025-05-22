package com.storage.cloudbackup.logic.model.io.writer

import org.json.JSONObject

interface Writer<T> {
    fun write(input: T): JSONObject
}