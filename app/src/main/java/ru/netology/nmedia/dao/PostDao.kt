package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY published DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun update(id: Long, content: String)

    fun save(post: PostEntity) =
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post.id, post.content)

    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun like(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
        shares = shares + 1
        WHERE id = :id
        """
    )
    fun share(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
        views = views + 1
        WHERE id = :id
        """
    )
    fun view(id: Long)


    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun delete(id: Long)
}