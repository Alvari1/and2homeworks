package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: ULong,
    val author: String,
    val content: String,
    val published: Long,
    val likes: ULong = 0U,
    val likedByMe: Boolean = false,
    val shares: ULong = 0U,
    val views: ULong = 0U,
    val url: String? = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
)