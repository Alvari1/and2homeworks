package ru.netology.nmedia.dto

data class Post(
    val id: ULong,
    val author: String,
    val content: String,
    val published: Long,
    val likes: ULong = 0U,
    val likedByMe: Boolean = false,
    val shares: ULong = 0U,
    val views: ULong = 0U,
)