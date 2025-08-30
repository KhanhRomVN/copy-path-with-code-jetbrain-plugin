package com.khanhromvn.copypathwithcode.toolWindow

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeStructure
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project
import com.khanhromvn.copypathwithcode.persistence.DataStorage

class FolderTreeStructure(private val project: Project) : AbstractTreeStructure() {
    
    override fun getRootElement(): Any {
        return RootElement()
    }
    
    override fun getParentElement(element: Any): Any? {
        return when (element) {
            is RootElement -> null
            is FolderTreeNode -> rootElement
            is FileTreeNode -> element.parent
            else -> null
        }
    }
    
    override fun getChildElements(element: Any): Array<Any> {
        return when (element) {
            is RootElement -> {
                DataStorage.loadFolders().map { folder ->
                    FolderTreeNode(folder, project)
                }.toTypedArray()
            }
            is FolderTreeNode -> {
                val folder = element.value ?: return emptyArray()
                folder.files.map { filePath ->
                    FileTreeNode(filePath, element, project)
                }.toTypedArray()
            }
            else -> emptyArray()
        }
    }
    
    override fun isAlwaysLeaf(element: Any): Boolean {
        return element is FileTreeNode
    }
    
    override fun createDescriptor(element: Any, parentDescriptor: NodeDescriptor<*>?): NodeDescriptor<*> {
        return object : NodeDescriptor<Any>(project, parentDescriptor) {
            override fun update(): Boolean {
                // Return true if the presentation was updated
                return false
            }
            
            override fun getElement(): Any = element
        }
    }
    
    override fun hasSomethingToCommit(): Boolean = false
    
    override fun commit() {
        // No implementation needed
    }
    
    inner class RootElement
}