package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: ULong)
    fun view(Id: ULong)
    fun shareById(id: ULong)
    fun removeById(id: ULong)
}