package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.khanhromvn.copypathwithcode.model.CopiedFile
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils
import java.nio.charset.StandardCharsets

class CopyFolderContentsAction : AnAction() {
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
            "Select folder to copy contents",
            "Copy Folder Contents",
            Messages.getQuestionIcon(),
            folderNames,
            folderNames.first()
        )
        
        if (selectedIndex == -1) return // User cancelled
        
        ApplicationManager.getApplication().invokeLater {
            val folder = folders[selectedIndex]
            val copiedFiles = mutableListOf<CopiedFile>()
            
            folder.files.forEach { filePath ->
                val file = com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(filePath)
                if (file != null && file.exists()) {
                    try {
                        val content = String(file.contentsToByteArray(), StandardCharsets.UTF_8)
                        copiedFiles.add(CopiedFile(file.name, file.path, content))
                    } catch (ex: Exception) {
                        // Skip files that can't be read
                    }
                }
            }
            
            if (copiedFiles.isNotEmpty()) {
                ClipboardUtils.copyToClipboard(copiedFiles)
                Messages.showInfoMessage("Copied ${copiedFiles.size} files from \"${folder.name}\"", "Copy Path with Code")
            } else {
                Messages.showWarningDialog(project, "No files to copy in \"${folder.name}\"", "Copy Path with Code")
            }
        }
    }
}