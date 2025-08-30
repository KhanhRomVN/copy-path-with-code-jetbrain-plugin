package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.FileUtils

class RemoveFileFromFolderAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val currentFile = FileUtils.getCurrentFile(project) ?: return
        
        val folders = DataStorage.loadFolders()
        if (folders.isEmpty()) {
            Messages.showInfoMessage("No folders available.", "Copy Path with Code")
            return
        }
        
        val folderNames = folders.map { it.name }.toTypedArray()
        val selectedIndex = Messages.showChooseDialog(
            project,
            "Select folder to remove file",
            "Remove File from Folder",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.first()
        )
        
        if (selectedIndex == -1) return // User cancelled
        
        ApplicationManager.getApplication().invokeLater {
            val folder = folders[selectedIndex]
            if (folder.files.contains(currentFile.path)) {
                val updatedFiles = folder.files.toMutableList()
                updatedFiles.remove(currentFile.path)
                folder.files = updatedFiles
                
                val updatedFolders = folders.toMutableList()
                DataStorage.saveFolders(updatedFolders)
                Messages.showInfoMessage("File removed from \"${folder.name}\"", "Copy Path with Code")
            } else {
                Messages.showInfoMessage("File not found in \"${folder.name}\"", "Copy Path with Code")
            }
        }
    }
}