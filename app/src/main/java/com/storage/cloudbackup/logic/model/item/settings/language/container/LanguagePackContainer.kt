package com.storage.cloudbackup.logic.model.item.settings.language.container

import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack

class LanguagePackContainer {
    val packs: MutableMap<String, LanguagePack> = mutableMapOf()
}