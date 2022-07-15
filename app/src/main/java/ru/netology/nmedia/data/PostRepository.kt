package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    val currentPost: MutableLiveData<Post?>

    fun like(postId: Long)

    fun share(postId: Long)

    fun view(postId: Long)

    fun delete(postId: Long)

    fun save(post: Post)

    companion object {
        const val NEW_POST_ID: Long = 0L
    }
}