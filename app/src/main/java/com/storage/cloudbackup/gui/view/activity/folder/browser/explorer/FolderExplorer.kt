package com.storage.cloudbackup.gui.view.activity.folder.browser.explorer

interface FolderExplorer {
    val modes : List<ModeItem>

    fun openFolder(path: String) : List<String>?
    fun openRootFolder(modeId: Int) : Pair<List<String>?, String>?
}