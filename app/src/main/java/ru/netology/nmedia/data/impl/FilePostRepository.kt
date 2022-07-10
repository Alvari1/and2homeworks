package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class FilePostRepository(
    private val application: Application
) : PostRepository {
    private var nextId: ULong = 11U

    override val data: MutableLiveData<List<Post>>

    override val shareEvent = SingleLiveEvent<String>()
    override val currentPost = MutableLiveData<Post?>(null)
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var posts
        get() = checkNotNull(data.value) { "Data value should not be null" }
        set(value) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else emptyList<Post>()


        data = MutableLiveData(posts)
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

    override fun view(postId: ULong) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(views = it.shares + 1U)
        }
    }

    override fun share(postId: ULong) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1U)
                .apply { shareEvent.value = it.content }
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

    private companion object {
        const val FILE_NAME = "posts.json"
    }
}