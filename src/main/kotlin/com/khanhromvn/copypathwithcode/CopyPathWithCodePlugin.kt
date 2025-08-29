package com.khanhromvn.copypathwithcode

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.khanhromvn.copypathwithcode.persistence.DataStorage

class CopyPathWithCodePlugin : ProjectActivity {
    override suspend fun execute(project: Project) {
        // Load persisted data on startup
        DataStorage.loadFolders()
    }
}