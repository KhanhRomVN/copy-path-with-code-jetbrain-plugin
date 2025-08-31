package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.khanhromvn.copypathwithcode.model.CopiedFile
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils
import com.khanhromvn.copypathwithcode.utils.FileUtils
import com.intellij.openapi.util.SystemInfo
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class CopyPathWithContentAction : AnAction() {
    
    init {
        val shortcutKeyStroke = if (SystemInfo.isMac) {
            KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_DOWN_MASK or InputEvent.ALT_DOWN_MASK)
        } else {
            KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK or InputEvent.ALT_DOWN_MASK)
        }
        registerCustomShortcutSet(CustomShortcutSet(shortcutKeyStroke), null)
    }
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val file = FileUtils.getCurrentFile(project) ?: return

        val selectionModel = editor.selectionModel
        val document = editor.document

        val displayPath: String
        val content: String

        // Get relative path from project base directory
        val baseDir = project.baseDir
        val relativePath = VfsUtilCore.getRelativePath(file, baseDir, '/') ?: file.path

        if (selectionModel.hasSelection()) {
            val startLine = document.getLineNumber(selectionModel.selectionStart) + 1
            val endLine = document.getLineNumber(selectionModel.selectionEnd) + 1
            displayPath = "$relativePath:$startLine-$endLine"
            content = selectionModel.selectedText ?: ""
        } else {
            displayPath = relativePath
            content = document.text
        }

        val copiedFile = CopiedFile(displayPath, file.path, content)
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.copyToClipboard(listOf(copiedFile))
        }
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = project?.let { FileEditorManager.getInstance(it).selectedTextEditor }
        val file = project?.let { FileUtils.getCurrentFile(it) }
        
        e.presentation.isEnabledAndVisible = project != null && editor != null && file != null
        
        if (editor?.selectionModel?.hasSelection() == true) {
            e.presentation.text = "Copy Selection and Path"
            e.presentation.description = "Copy selected text and file path to clipboard"
        } else {
            e.presentation.text = "Copy File and Path"
            e.presentation.description = "Copy entire file content and path to clipboard"
        }
    }
}