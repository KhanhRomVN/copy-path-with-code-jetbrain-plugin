package com.khanhromvn.copypathwithcode.toolWindow

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.util.treeView.AbstractTreeStructure
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
                DataStorage.loadFolders().map { FolderTreeNode(it, project) }.toTypedArray()
            }
            is FolderTreeNode -> {
                element.folder.files.map { filePath ->
                    FileTreeNode(filePath, element, project)
                }.toTypedArray()
            }
            else -> emptyArray()
        }
    }

    override fun isAlwaysLeaf(element: Any): Boolean {
        return element is FileTreeNode
    }

    override fun createDescriptor(element: Any, parentElement: Any?): Any {
        return element
    }

    override fun getElementById(p0: Any?): Any? {
        return null
    }

    override fun commit() {
        // No implementation needed
    }

    inner class RootElement
}