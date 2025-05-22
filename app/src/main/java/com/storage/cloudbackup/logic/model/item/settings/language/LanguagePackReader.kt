package com.storage.cloudbackup.logic.model.item.settings.language

class LanguagePackReader {
    fun fromString(text: String): LanguagePack {
        val map = mutableMapOf<String, String>()

        val lines = text.split("\n")
        for (i in 1 until lines.size){
            readLine(lines[i], map)
        }

        val index = lines[0].indexOf(":")
        val code = lines[0].substring(0, index)
        val name = lines[0].substring(index + 1)

        return StandardLanguagePack(map, name, code)
    }

    private fun readLine(line: String, output: MutableMap<String, String>) {
        if(line.trim().isEmpty()) return
        if(line.startsWith("#")) return

        val pair = line.split(":")
        val key = pair[0].trim()
        val value = pair[1].trim()

        output[key] = value
    }
}