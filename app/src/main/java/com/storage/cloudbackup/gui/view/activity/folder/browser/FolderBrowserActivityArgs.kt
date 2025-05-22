package com.storage.cloudbackup.gui.view.activity.folder.browser

import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.gui.view.activity.folder.browser.explorer.FolderExplorer
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

data class FolderBrowserActivityArgs (
    val folderExplorer: FolderExplorer,
    val currentFolder: String,
    val languagePermit: LanguagePermit,
    val folderSelectedListener: ArgsEvent<String>)
