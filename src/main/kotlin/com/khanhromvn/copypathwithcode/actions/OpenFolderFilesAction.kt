package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.khanhromvn.copypathwithcode.persistence.DataStorage

class OpenFolderFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        val folders = DataStorage.loadFolders()
        if (folders.isEmpty()) {
            Messages.showInfoMessage("No folders available.", "Copy Path with Code")
            return
        }
        
        val folderNames = folders.map { it.name }.toTypedArray()
        val selectedFolder = Messages.showChooseDialog(
            project,
            "Select folder to open files",
            "Open Folder Files",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.firstOrNull()
        ) ?: return
        
        ApplicationManager.getApplication().invokeLater {
            val folder = folders.first { it.name == selectedFolder }
            val fileEditorManager = FileEditorManager.getInstance(project)
            
            // Close all existing editors first
            fileEditorManager.openFiles.forEach { fileEditorManager.closeFile(it) }
            
            // Open all files in the folder
            folder.files.forEach { filePath ->
                val file = LocalFileSystem.getInstance().findFileByPath(filePath)
                if (file != null) {
                    fileEditorManager.openFile(file, true)
                }
            }
            
            Messages.showInfoMessage("Opened ${folder.files.size} files from \"${folder.name}\"", "Copy Path with Code")
        }
    }
}