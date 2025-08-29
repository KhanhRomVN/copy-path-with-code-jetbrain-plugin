package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.model.Folder
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.FileUtils

class AddFileToFolderAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val currentFile = FileUtils.getCurrentFile(project) ?: return
        
        val folders = DataStorage.loadFolders()
        if (folders.isEmpty()) {
            Messages.showInfoMessage("No folders available. Create a folder first.", "Copy Path with Code")
            return
        }
        
        val folderNames = folders.map { it.name }.toTypedArray()
        val selectedFolder = Messages.showChooseDialog(
            project,
            "Select folder to add file",
            "Add File to Folder",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.firstOrNull()
        ) ?: return
        if (selectedFolder == null) return
        
        ApplicationManager.getApplication().invokeLater {
            val folder = folders.first { it.name == selectedFolder }
            if (!folder.files.contains(currentFile.path)) {
                val updatedFiles = folder.files.toMutableList()
                updatedFiles.add(currentFile.path)
                folder.files = updatedFiles
                
                val updatedFolders = folders.toMutableList()
                DataStorage.saveFolders(updatedFolders)
                Messages.showInfoMessage("File added to \"${folder.name}\"", "Copy Path with Code")
            } else {
                Messages.showInfoMessage("File already exists in \"${folder.name}\"", "Copy Path with Code")
            }
        }
    }
}