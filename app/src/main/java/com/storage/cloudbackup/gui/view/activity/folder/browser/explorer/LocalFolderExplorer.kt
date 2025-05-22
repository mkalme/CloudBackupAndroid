package com.storage.cloudbackup.gui.view.activity.folder.browser.explorer

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.storage.cloudbackup.logic.model.item.settings.language.LanguagePack
import java.io.File

class LocalFolderExplorer(languagePack: LanguagePack, context: Context) : FolderExplorer {
    private val _modes: List<ModeItem>
    override val modes: List<ModeItem>
        get() = _modes

    private val internal: File?
    private val sd: File?

    private enum class ModeType(val id: Int) {
        Internal(0),
        SD(1)
    }

    init {
        val roots = getRoots(context)
        internal = roots[0]
        sd = roots[1]

        val modes = mutableListOf<ModeItem>()
        modes.add(ModeItem(languagePack.getTranslation("localFolderExplorer.internal"), ModeType.Internal.id))
        if(sd != null) modes.add(ModeItem(languagePack.getTranslation("localFolderExplorer.sd"), ModeType.SD.id))

        _modes = modes
    }

    private fun getRoots(context: Context): Array<File?> {
        val folders = ContextCompat.getExternalFilesDirs(context, null)
        val output = arrayOfNulls<File>(2)

        output[0] = Environment.getExternalStorageDirectory()
        output[1] = folders[1]?.parentFile?.parentFile?.parentFile?.parentFile

        return output
    }

    override fun openFolder(path: String): List<String>? {
        if(path == internal?.parent || path == sd?.parent) return null

        val currentFolder = File(path)
        if(!currentFolder.exists()) return null

        val folders = currentFolder.listFiles { obj: File -> obj.isDirectory } ?: return null

        val output: MutableList<String> = mutableListOf()
        folders.forEach {
            output.add(it.absolutePath)
        }

        return output
    }

    override fun openRootFolder(modeId: Int): Pair<List<String>?, String>? {
        return when(modeId){
            ModeType.Internal.id -> {
                if(internal == null) return null
                return Pair(openFolder(internal.absolutePath), internal.absolutePath)
            }
            ModeType.SD.id -> {
                if(sd == null) return null
                return Pair(openFolder(sd.absolutePath), sd.absolutePath)
            }
            else -> null
        }
    }
}
