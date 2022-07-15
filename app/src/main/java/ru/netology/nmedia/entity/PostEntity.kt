package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: Long = 0,
    val likes: Long = 0,
    val likedByMe: Boolean = false,
    val shares: Long = 0,
    val views: Long = 0,
    val url: String? = null
) {

    fun toDto() = Post(id, author, content, published, likes, likedByMe, shares, views, url)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.content,
                dto.published,
                dto.likes,
                dto.likedByMe,
                dto.shares,
                dto.views,
                dto.url
            )

    }
}