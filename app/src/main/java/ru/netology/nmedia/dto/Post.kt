package ru.netology.nmedia.dto

data class Post(
    val id: ULong,
    val author: String,
    val content: String,
    val published: Long,
    var likes: ULong = 0U,
    var likedByMe: Boolean = false,
    var shares: ULong = 0U,
    var views: ULong = 0U,
)