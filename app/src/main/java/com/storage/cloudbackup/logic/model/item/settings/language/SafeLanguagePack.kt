package com.storage.cloudbackup.logic.model.item.settings.language

class SafeLanguagePack(private val primary: LanguagePack, private val secondary: LanguagePack) :
    LanguagePack {
    override val name: String
        get() = primary.name
    override val code: String
        get() = primary.code

    override fun getTranslation(key: String): String {
        val primaryTranslation = primary.getTranslation(key)

        if(primaryTranslation.isEmpty()) {
            val secondaryTranslation = secondary.getTranslation(key)
            if(secondaryTranslation.isNotEmpty()) return secondaryTranslation
        }

        return primaryTranslation
    }
}