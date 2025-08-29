package com.khanhromvn.copypathwithcode.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.Messages
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import com.khanhromvn.copypathwithcode.utils.ClipboardUtils

class ClearClipboardAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ApplicationManager.getApplication().invokeLater {
            ClipboardUtils.clearClipboard()
            DataStorage.saveFolders(emptyList())
            Messages.showInfoMessage("Clipboard cleared", "Copy Path with Code")
        }
    }
}