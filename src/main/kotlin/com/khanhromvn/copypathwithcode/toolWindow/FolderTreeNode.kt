package com.khanhromvn.copypathwithcode.toolWindow

import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.khanhromvn.copypathwithcode.model.Folder
import java.util.*

class FolderTreeNode(folder: Folder, project: Project) : AbstractTreeNode<Folder>(project, folder) {
    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return myValue.files.map { filePath ->
            FileTreeNode(filePath, this, myProject)
        }
    }
    
    override fun update(presentation: com.intellij.ui.SimpleTextAttributes) {
        presentation.append(myValue.name)
    }
}

class FileTreeNode(
    private val filePath: String,
    private val parent: FolderTreeNode,
    project: Project
) : AbstractTreeNode<Any>(project, filePath) {
    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return emptyList()
    }
    
    override fun update(presentation: com.intellij.ui.SimpleTextAttributes) {
        val file = LocalFileSystem.getInstance().findFileByPath(filePath)
        presentation.append(file?.name ?: filePath)
    }
}