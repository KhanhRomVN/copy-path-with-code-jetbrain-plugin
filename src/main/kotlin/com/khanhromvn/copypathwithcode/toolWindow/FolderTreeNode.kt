package com.khanhromvn.copypathwithcode.toolWindow

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.SimpleTextAttributes
import com.khanhromvn.copypathwithcode.model.Folder

class FolderTreeNode(folder: Folder, project: Project) : AbstractTreeNode<Folder>(project, folder) {
    
    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        val folder = value ?: return emptyList()
        return folder.files.map { filePath ->
            FileTreeNode(filePath, this, project!!)
        }
    }
    
    override fun update(presentation: PresentationData) {
        val folder = value
        if (folder != null) {
            presentation.addText(folder.name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        }
    }
}

class FileTreeNode(
    val filePath: String,
    private val parent: FolderTreeNode,
    project: Project
) : AbstractTreeNode<String>(project, filePath) {
    
    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return emptyList()
    }
    
    override fun update(presentation: PresentationData) {
        val file = LocalFileSystem.getInstance().findFileByPath(filePath)
        val fileName = file?.name ?: filePath.substringAfterLast('/')
        presentation.addText(fileName, SimpleTextAttributes.REGULAR_ATTRIBUTES)
    }
}