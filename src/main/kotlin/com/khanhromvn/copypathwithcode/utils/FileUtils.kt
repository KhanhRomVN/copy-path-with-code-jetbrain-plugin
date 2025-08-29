package com.khanhromvn.copypathwithcode.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object FileUtils {
    fun getCurrentFile(project: Project): VirtualFile? {
        val fileEditorManager = FileEditorManager.getInstance(project)
        return fileEditorManager.selectedFiles.firstOrNull()
    }
}