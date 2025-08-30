package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.persistence.DataStorage

class DeleteFolderAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        val folders = DataStorage.loadFolders()
        if (folders.isEmpty()) {
            Messages.showInfoMessage("No folders available.", "Copy Path with Code")
            return
        }
        
        val folderNames = folders.map { it.name }.toTypedArray()
        val selectedIndex = Messages.showChooseDialog(
            project,
            "Select folder to delete",
            "Delete Folder",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.first()
        )
        
        if (selectedIndex == -1) return // User cancelled
        
        val selectedFolderName = folderNames[selectedIndex]
        
        val confirm = Messages.showYesNoDialog(
            project,
            "Are you sure you want to delete \"$selectedFolderName\"?",
            "Confirm Delete",
            Messages.getQuestionIcon()
        )
        
        if (confirm == Messages.YES) {
            ApplicationManager.getApplication().invokeLater {
                val updatedFolders = folders.filter { it.name != selectedFolderName }.toMutableList()
                DataStorage.saveFolders(updatedFolders)
                Messages.showInfoMessage("Folder \"$selectedFolderName\" deleted", "Copy Path with Code")
            }
        }
    }
}