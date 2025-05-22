package com.storage.cloudbackup.logic.model.item.settings.language.container

import android.content.Context
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePackReader
import com.storage.cloudbackup.logic.model.item.settings.language.SafeLanguagePack

class LanguagePackContainerReader {
    private val languagePackReader = LanguagePackReader()

    fun readFromAssetsDirectory(path: String, context: Context): LanguagePackContainer {
        val assetManager = context.assets
        val files = assetManager.list(path)

        val output = LanguagePackContainer()
        files?.forEach {
            val fullPath = "${path}/${it}"
            val text = assetManager.open(fullPath).bufferedReader().use{ jt ->
                jt.readText()
            }

            val languagePack = languagePackReader.fromString(text)
            output.packs[languagePack.code] = languagePack
        }

        val primaryLanguage = output.packs["en"]!!
        output.packs.forEach{
            if (it.value.code != primaryLanguage.code) output.packs[it.key] = SafeLanguagePack(it.value, primaryLanguage)
        }

        return output
    }
}