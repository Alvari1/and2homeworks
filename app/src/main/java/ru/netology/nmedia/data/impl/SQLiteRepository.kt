package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.dto.Post

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {
    override val data: MutableLiveData<List<Post>>
    override val shareEvent = SingleLiveEvent<String>()
    override val currentPost = MutableLiveData<Post?>(null)

    private var posts
        get() = checkNotNull(data.value) { "Data value should not be null" }
        set(value) {
            data.value = value
        }

    init {
        val posts: List<Post> = dao.getAll()
        data = MutableLiveData(posts)
    }

    override fun like(postId: ULong) {
        dao.likeById(postId)
        posts = posts.map {
            if (it.id != postId) it
            else {
                val likes = if (it.likedByMe) {
                    if (it.likes.equals(0U)) 0U else it.likes - 1U
                } else it.likes + 1U

                it.copy(likedByMe = !it.likedByMe, likes = likes)
            }
        }
    }

    override fun view(postId: ULong) {
        dao.view(postId)
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(views = it.views + 1U)
        }
    }

    override fun share(postId: ULong) {
        dao.shareById(postId)
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1U)
                .apply { shareEvent.value = it.content }
        }
    }

    override fun delete(postId: ULong) {
        dao.removeById(postId)
        posts = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        val saved = dao.save(post)
        if (post.id.equals(PostRepository.NEW_POST_ID)) {
            insert(saved)
        } else {
            update(saved)
        }
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post) + posts
    }
}