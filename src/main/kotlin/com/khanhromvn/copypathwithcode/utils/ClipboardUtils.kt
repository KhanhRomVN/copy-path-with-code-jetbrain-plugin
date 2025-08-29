package com.khanhromvn.copypathwithcode.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ide.CopyPasteManager
import com.khanhromvn.copypathwithcode.model.CopiedFile
import java.awt.datatransfer.StringSelection

object ClipboardUtils {
    fun copyToClipboard(files: List<CopiedFile>) {
        val combined = files.joinToString("\n\n---\n\n") { file ->
            "${file.displayPath}\n\n${file.content}"
        }
        
        ApplicationManager.getApplication().runWriteAction {
            CopyPasteManager.getInstance().setContents(StringSelection(combined))
        }
    }
    
    fun clearClipboard() {
        ApplicationManager.getApplication().runWriteAction {
            CopyPasteManager.getInstance().setContents(StringSelection(""))
        }
    }
}