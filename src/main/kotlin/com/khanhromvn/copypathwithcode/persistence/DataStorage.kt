package com.khanhromvn.copypathwithcode.persistence

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.khanhromvn.copypathwithcode.model.Folder
import com.khanhromvn.copypathwithcode.model.AppState

@State(name = "CopyPathWithCode", storages = [Storage("copy-path-with-code.xml")])
class DataStorage : PersistentStateComponent<AppState> {
    private var state = AppState()

    companion object {
        private val instance: DataStorage
            get() = ApplicationManager.getApplication().getService(DataStorage::class.java)

        fun loadFolders(): List<Folder> {
            return instance.state.folders
        }

        fun saveFolders(folders: List<Folder>) {
            instance.state.folders = folders
        }
    }

    override fun getState(): AppState = state

    override fun loadState(state: AppState) {
        this.state = state
    }
}