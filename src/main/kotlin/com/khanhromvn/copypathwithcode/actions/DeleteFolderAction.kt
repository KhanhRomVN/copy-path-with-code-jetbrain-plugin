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
        val selectedFolder = Messages.showChooseDialog(
            project,
            "Select folder to delete",
            "Delete Folder",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.firstOrNull()
        ) ?: return
        
        val confirm = Messages.showYesNoDialog(
            project,
            "Are you sure you want to delete \"$selectedFolder\"?",
            "Confirm Delete",
            Messages.getQuestionIcon()
        )
        
        if (confirm == Messages.YES) {
            ApplicationManager.getApplication().invokeLater {
                val updatedFolders = folders.filter { it.name != selectedFolder }.toMutableList()
                DataStorage.saveFolders(updatedFolders)
                Messages.showInfoMessage("Folder \"$selectedFolder\" deleted", "Copy Path with Code")
            }
        }
    }
}