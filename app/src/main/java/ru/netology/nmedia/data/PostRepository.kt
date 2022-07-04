package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(postId: ULong)

    fun share(postId: ULong)

    fun view(postId: ULong)
}