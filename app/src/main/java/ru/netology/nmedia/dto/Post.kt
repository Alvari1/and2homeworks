package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likes: Long = 0,
    val likedByMe: Boolean = false,
    val shares: Long = 0,
    val views: Long = 0,
    val url: String? = null,
)