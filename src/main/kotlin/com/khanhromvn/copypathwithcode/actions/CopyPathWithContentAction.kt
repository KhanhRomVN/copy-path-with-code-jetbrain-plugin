package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.model.CopiedFile
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils
import com.khanhromvn.copypathwithcode.utils.FileUtils

class CopyPathWithContentAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        
        val file = FileUtils.getCurrentFile(project) ?: return
        val selectionModel = editor.selectionModel
        
        val (displayPath, content) = if (selectionModel.hasSelection()) {
            val startLine = editor.offsetToLogicalPosition(selectionModel.selectionStart).line + 1
            val endLine = editor.offsetToLogicalPosition(selectionModel.selectionEnd).line + 1
            "${file.name}:$startLine-$endLine" to selectionModel.selectedText ?: ""
        } else {
            file.name to editor.document.text
        }

        val copiedFile = CopiedFile(displayPath, file.path, content)
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.copyToClipboard(listOf(copiedFile))
            Messages.showInfoMessage("Copied to clipboard", "Copy Path with Code")
        }
    }
}