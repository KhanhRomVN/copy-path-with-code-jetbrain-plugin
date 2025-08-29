package com.khanhromvn.copypathwithcode.toolWindow

import com.intellij.ide.util.treeView.TreeVisitor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import com.khanhromvn.copypathwithcode.persistence.DataStorage
import javax.swing.JTree
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeSelectionModel

class CopyPathToolWindow : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowPanel = SimpleToolWindowPanel(true, true)
        val tree = createTree(project)
        
        toolWindowPanel.setContent(JBScrollPane(tree))
        toolWindow.contentManager.addContent(
            toolWindow.contentManager.factory.createContent(toolWindowPanel, "", false)
        )
    }
    
    private fun createTree(project: Project): JTree {
        val tree = Tree(DefaultTreeModel(null))
        tree.model = DefaultTreeModel(FolderTreeStructure(project).rootElement as? javax.swing.tree.TreeNode)
        tree.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        tree.isRootVisible = false
        
        // Add double-click listener to open files
        tree.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: java.awt.event.MouseEvent) {
                if (e.clickCount == 2) {
                    val selectedNode = tree.lastSelectedPathComponent
                    if (selectedNode is FileTreeNode) {
                        val file = com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(selectedNode.filePath)
                        file?.let {
                            com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project).openFile(it, true)
                        }
                    }
                }
            }
        })
        
        return tree
    }
    
    override fun shouldBeAvailable(project: Project): Boolean {
        return true
    }
}