package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class ClearClipboardAction : AnAction() {
    
    init {
        // Register custom shortcut to use Ctrl+Alt+Z
        val shortcutKeyStroke = if (SystemInfo.isMac) {
            // For macOS: Cmd+Alt+Z
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, 
                InputEvent.META_DOWN_MASK or InputEvent.ALT_DOWN_MASK
            )
        } else {
            // For Windows/Linux: Ctrl+Alt+Z
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, 
                InputEvent.CTRL_DOWN_MASK or InputEvent.ALT_DOWN_MASK
            )
        }
        
        registerCustomShortcutSet(CustomShortcutSet(shortcutKeyStroke), null)
    }
    
    
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.clearClipboard()
            DataStorage.saveFolders(emptyList())
            
            // Show notification with the shortcut info
            val shortcutText = if (SystemInfo.isMac) "⌥⌘Z" else "Ctrl+Alt+Z"
            Messages.showInfoMessage(
                "Clipboard cleared!\nShortcut: $shortcutText", 
                "Copy Path with Code"
            )
        }
    }
}