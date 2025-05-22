package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.settings

import com.storage.cloudbackup.logic.model.io.writer.Writer
import org.json.JSONObject

class SettingsWriter : Writer<Settings> {
    override fun write(input: Settings): JSONObject {
        val output = JSONObject()
        output.put("Credentials", input.accountCredentials)
        output.put("Bucket", input.bucket)
        output.put("RootFolder", input.rootFolder)

        return output
    }
}