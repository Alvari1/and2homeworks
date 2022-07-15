package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {
    override fun getAll() = db.query(
        PostsTable.POSTS_NAME,
        PostsTable.ALL_POSTS_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.PostsColumn.Published.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun removeById(id: ULong) {
        db.delete(
            PostsTable.POSTS_NAME,
            "${PostsTable.PostsColumn.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun view(id: ULong) {
        db.execSQL(
            """
            UPDATE ${PostsTable.POSTS_NAME} SET
                views = views + 1 
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }


    override fun likeById(id: ULong) {
        db.execSQL(
            """
            UPDATE ${PostsTable.POSTS_NAME} SET
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: ULong) {
        db.execSQL(
            """
            UPDATE ${PostsTable.POSTS_NAME} SET
                shares = shares + 1 
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.PostsColumn.Author.columnName, post.author)
            put(PostsTable.PostsColumn.Content.columnName, post.content)
            put(PostsTable.PostsColumn.Published.columnName, post.published)
            put(PostsTable.PostsColumn.Url.columnName, post.url)
        }

        val id = if (post.id != PostRepository.NEW_POST_ID) {
            db.update(
                PostsTable.POSTS_NAME,
                values,
                "${PostsTable.PostsColumn.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(
                PostsTable.POSTS_NAME,
                null,
                values
            )
        }

        return db.query(
            PostsTable.POSTS_NAME,
            PostsTable.ALL_POSTS_COLUMNS_NAMES,
            "${PostsTable.PostsColumn.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }
}