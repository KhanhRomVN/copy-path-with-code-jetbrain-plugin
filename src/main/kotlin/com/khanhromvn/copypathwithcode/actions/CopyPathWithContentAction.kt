package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.khanhromvn.copypathwithcode.model.CopiedFile
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils
import com.khanhromvn.copypathwithcode.utils.FileUtils
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class CopyPathWithContentAction : AnAction() {
    
    init {
        // Register custom shortcut to use Ctrl+Alt+C
        val shortcutKeyStroke = if (SystemInfo.isMac) {
            // For macOS: Cmd+Alt+C
            KeyStroke.getKeyStroke(
                KeyEvent.VK_C, 
                InputEvent.META_DOWN_MASK or InputEvent.ALT_DOWN_MASK
            )
        } else {
            // For Windows/Linux: Ctrl+Alt+C
            KeyStroke.getKeyStroke(
                KeyEvent.VK_C, 
                InputEvent.CTRL_DOWN_MASK or InputEvent.ALT_DOWN_MASK
            )
        }
        
        registerCustomShortcutSet(CustomShortcutSet(shortcutKeyStroke), null)
    }
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return

        val file = FileUtils.getCurrentFile(project) ?: return
        val selectionModel = editor.selectionModel

        val displayPath: String
        val content: String

        if (selectionModel.hasSelection()) {
            val startLine = editor.offsetToLogicalPosition(selectionModel.selectionStart).line + 1
            val endLine = editor.offsetToLogicalPosition(selectionModel.selectionEnd).line + 1
            displayPath = "${file.name}:$startLine-$endLine"
            content = selectionModel.selectedText ?: ""
        } else {
            displayPath = file.name
            content = editor.document.text
        }

        val copiedFile = CopiedFile(displayPath, file.path, content)
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.copyToClipboard(listOf(copiedFile))
            
            // Show notification with the shortcut info
            val shortcutText = if (SystemInfo.isMac) "⌥⌘C" else "Ctrl+Alt+C"
            Messages.showInfoMessage(
                project,
                "File content copied to clipboard!\nShortcut: $shortcutText", 
                "Copy Path with Code"
            )
        }
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = project?.let { FileEditorManager.getInstance(it).selectedTextEditor }
        val file = project?.let { FileUtils.getCurrentFile(it) }
        
        e.presentation.isEnabledAndVisible = project != null && editor != null && file != null
        
        // Update text based on selection
        if (editor?.selectionModel?.hasSelection() == true) {
            e.presentation.text = "Copy Selection and Path"
            e.presentation.description = "Copy selected text and file path to clipboard"
        } else {
            e.presentation.text = "Copy File and Path"
            e.presentation.description = "Copy entire file content and path to clipboard"
        }
    }
}