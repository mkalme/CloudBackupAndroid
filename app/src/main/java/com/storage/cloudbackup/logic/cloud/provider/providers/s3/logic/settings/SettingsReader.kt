package com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.settings

import com.storage.cloudbackup.logic.model.io.reader.Reader
import org.json.JSONObject

class SettingsReader : Reader<Settings> {
    override fun read(data: JSONObject): Settings {
        val output = Settings()
        output.accessKey = data.getString("AccessKey")
        output.secretKey = data.getString("SecretKey")
        output.bucket = data.getString("Bucket")
        output.rootFolder = data.getString("RootFolder")

        return output
    }
}