package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.model.Folder
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.FileUtils
import java.util.*

class CreateFolderAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        val name = Messages.showInputDialog(
            project,
            "Enter folder name",
            "Create Folder",
            Messages.getQuestionIcon(),
            "My Code Folder",
            InputValidator { input -> input.isNotBlank() }
        ) ?: return

        val openFiles = FileEditorManager.getInstance(project).openFiles
        val fileUris = openFiles.map { it.path }
        
        val folder = Folder(
            id = UUID.randomUUID().toString(),
            name = name,
            files = fileUris
        )
        
        ApplicationManager.getApplication().invokeLater {
            val currentFolders = DataStorage.loadFolders().toMutableList()
            currentFolders.add(folder)
            DataStorage.saveFolders(currentFolders)
            Messages.showInfoMessage("Folder \"$name\" created with ${fileUris.size} files", "Copy Path with Code")
        }
    }
}