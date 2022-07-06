package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId: ULong = 11U

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = index.toULong() + 1U,
                author = "Alex",
                content = "some strange content here #${index.toULong() + 1U}",
                published = System.currentTimeMillis(),
                likes = 0U,
                shares = 0U,
                views = 0U,
                likedByMe = false
            )
        }
    )

    private val posts
        get() = checkNotNull(data.value) {
            "data value should not be null"
        }

    override fun like(postId: ULong) {
        data.value = posts.map {
            if (it.id != postId) it
            else {
                val likes = if (it.likedByMe) {
                    if (it.likes.equals(0U)) 0U else it.likes - 1U
                } else it.likes + 1U

                it.copy(likedByMe = !it.likedByMe, likes = likes)
            }
        }
    }

    override fun share(postId: ULong) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1U)
        }
    }

    override fun view(postId: ULong) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(views = it.shares + 1U)
        }
    }

    override fun delete(postId: ULong) {
        data.value = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id.equals(PostRepository.NEW_POST_ID)) {
            insert(post)
        } else {
            update(post)
        }
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts
    }
}