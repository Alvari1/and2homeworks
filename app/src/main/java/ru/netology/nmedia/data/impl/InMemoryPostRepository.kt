package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 1U,
            author = "Alex",
            content = "some strange content here",
            published = System.currentTimeMillis(),
            likes = 0U,
            shares = 0U,
            views = 0U,
            likedByMe = false
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "data value should not be null"
        }

        val likes = if (currentPost.likedByMe) {
            if (currentPost.likes.equals(0U)) 0U else currentPost.likes - 1U
        } else {
            currentPost.likes + 1U
        }

        val likedPost = currentPost.copy(likedByMe = !currentPost.likedByMe, likes = likes)
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "data value should not be null"
        }

        val sharedPost = currentPost.copy(shares = currentPost.shares + 1U)
        data.value = sharedPost
    }

}