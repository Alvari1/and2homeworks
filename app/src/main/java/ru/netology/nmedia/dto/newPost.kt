package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

data class NewPost(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: Long,

    @SerializedName("postAuthor")
    val postAuthor: String,

    @SerializedName("postContent")
    val postContent: String,
)