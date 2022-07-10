package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    val shareEvent: SingleLiveEvent<String>
    val currentPost: MutableLiveData<Post?>

    fun like(postId: ULong)

    fun share(postId: ULong)

    fun view(postId: ULong)

    fun delete(postId: ULong)

    fun save(post: Post)

    companion object {
        const val NEW_POST_ID: ULong = 0U
    }
}