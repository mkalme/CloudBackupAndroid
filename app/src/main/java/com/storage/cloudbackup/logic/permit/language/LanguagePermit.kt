package com.storage.cloudbackup.logic.permit.language

import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

interface LanguagePermit {
    val languageChangedListener: MutableList<EmptyEvent>
    val currentLanguage: LanguagePack
}