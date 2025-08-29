package com.khanhromvn.copypathwithcode.model

data class Folder(
    val id: String,
    var name: String,
    var files: List<String> = emptyList(), // List of file paths
    var color: String? = null
)