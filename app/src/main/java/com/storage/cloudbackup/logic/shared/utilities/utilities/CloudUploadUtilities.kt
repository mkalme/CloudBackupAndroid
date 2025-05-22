package com.storage.cloudbackup.logic.shared.utilities.utilities

object CloudUploadUtilities {
    fun createFullDirectoryPath(rootDirectory: String?, directory: String): String {
        if(rootDirectory == null) return ""

        if(rootDirectory.isNotEmpty() && directory.isNotEmpty()) return "${rootDirectory}/${directory}"
        if(rootDirectory.isNotEmpty() && directory.isEmpty()) return rootDirectory
        return directory
    }

    fun createFullFilePath(rootDirectory: String?, directory: String, file: String): String {
        val parent = createFullDirectoryPath(rootDirectory, directory)
        if(parent.isNotEmpty()) return "${parent}/${file}"
        return file
    }
}