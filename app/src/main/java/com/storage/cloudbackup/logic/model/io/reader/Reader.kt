package com.storage.cloudbackup.logic.model.io.reader

import org.json.JSONObject

interface Reader<T> {
    fun read(data: JSONObject): T
}