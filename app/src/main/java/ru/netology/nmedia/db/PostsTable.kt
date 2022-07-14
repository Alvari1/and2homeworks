package ru.netology.nmedia.db

object PostsTable {
    const val POSTS_NAME = "posts"

    val POSTS_DDL = """
        CREATE TABLE $POSTS_NAME (
            ${PostsColumn.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostsColumn.Author.columnName} TEXT NOT NULL,
            ${PostsColumn.Content.columnName} TEXT NOT NULL,
            ${PostsColumn.Published.columnName} INTEGER NOT NULL DEFAULT 0,
            ${PostsColumn.LikedByMe.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${PostsColumn.Likes.columnName} INTEGER NOT NULL DEFAULT 0,
            ${PostsColumn.Shares.columnName} INTEGER NOT NULL DEFAULT 0,
            ${PostsColumn.Views.columnName} INTEGER NOT NULL DEFAULT 0,
            ${PostsColumn.Url.columnName} TEXT DEFAULT NULL
        )
    """.trimIndent()

    val ALL_POSTS_COLUMNS_NAMES = PostsColumn.values().map {
        it.columnName
    }.toTypedArray()

    enum class PostsColumn(val columnName: String) {
        ID("id"),
        Author("author"),
        Content("content"),
        Published("published"),
        Likes("likes"),
        LikedByMe("likedByMe"),
        Shares("shares"),
        Views("views"),
        Url("url")
    }
}