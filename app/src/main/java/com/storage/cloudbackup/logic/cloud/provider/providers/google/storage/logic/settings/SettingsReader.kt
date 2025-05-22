package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.settings

import com.storage.cloudbackup.logic.model.io.reader.Reader
import org.json.JSONObject

class SettingsReader : Reader<Settings> {
    override fun read(data: JSONObject): Settings {
        val output = Settings()
        output.accountCredentials = data.getString("Credentials")
        output.bucket = data.getString("Bucket")
        output.rootFolder = data.getString("RootFolder")

        return output
    }
}