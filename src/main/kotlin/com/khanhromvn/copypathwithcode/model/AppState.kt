package com.khanhromvn.copypathwithcode.model

data class AppState(
    var folders: List<Folder> = emptyList(),
    var copiedFiles: List<CopiedFile> = emptyList()
)