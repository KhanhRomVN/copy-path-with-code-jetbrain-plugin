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
        val shortcutKeyStroke = if (SystemInfo.isMac) {
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK or InputEvent.ALT_DOWN_MASK)
        } else {
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK or InputEvent.ALT_DOWN_MASK)
        }
        registerCustomShortcutSet(CustomShortcutSet(shortcutKeyStroke), null)
    }
    
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.clearClipboard()
            DataStorage.saveFolders(emptyList())
            
            val shortcutText = if (SystemInfo.isMac) "⌥⌘Q" else "Ctrl+Alt+Q"
            Messages.showInfoMessage(
                "Clipboard cleared!\nShortcut: $shortcutText", 
                "Copy Path with Code"
            )
        }
    }
}