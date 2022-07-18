package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.dto.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.PostsColumn.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.PostsColumn.Author.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.PostsColumn.Content.columnName)),
    published = getLong(getColumnIndexOrThrow(PostsTable.PostsColumn.Published.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.PostsColumn.LikedByMe.columnName)) != 0,
    likes = getLong(getColumnIndexOrThrow(PostsTable.PostsColumn.Likes.columnName)),
    shares = getLong(getColumnIndexOrThrow(PostsTable.PostsColumn.Shares.columnName)),
    views = getLong(getColumnIndexOrThrow(PostsTable.PostsColumn.Views.columnName)),
    url = getString(getColumnIndexOrThrow(PostsTable.PostsColumn.Url.columnName))
)