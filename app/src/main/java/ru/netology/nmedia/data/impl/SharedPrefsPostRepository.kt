package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private var nextId: ULong = (GENERATED_POSTS_AMOUNT + 1).toULong()

    private val preferences = application.getSharedPreferences(
        "repo",
        Context.MODE_PRIVATE
    )

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = preferences.getString(POSTS_PREFERENCES_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override val shareEvent = SingleLiveEvent<String>()
    override val currentPost = MutableLiveData<Post?>(null)

    private var posts
        get() = checkNotNull(data.value) {
            "data value should not be null"
        }
        set(value) {
            preferences.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFERENCES_KEY, serializedPosts)
            }
            data.value = value
        }

    override fun like(postId: ULong) {
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

    override fun share(postId: ULong) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1U)
        }
    }

    override fun view(postId: ULong) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(views = it.shares + 1U)
        }
    }

    override fun delete(postId: ULong) {
        posts = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id.equals(PostRepository.NEW_POST_ID)) {
            insert(post)
        } else {
            update(post)
        }
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    companion object {
        const val GENERATED_POSTS_AMOUNT = 10
        const val POSTS_PREFERENCES_KEY = "posts"
    }
}