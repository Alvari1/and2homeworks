package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository(
) : PostRepository {
    private var nextId = (GENERATED_POSTS_AMOUNT + 1).toULong()

    override val data = MutableLiveData(
        List(GENERATED_POSTS_AMOUNT) { index ->
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

    override val shareEvent = SingleLiveEvent<String>()
    override val currentPost = MutableLiveData<Post?>(null)

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
            else it.copy(views = it.views + 1U)
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

    companion object {
        const val GENERATED_POSTS_AMOUNT = 10
    }
}