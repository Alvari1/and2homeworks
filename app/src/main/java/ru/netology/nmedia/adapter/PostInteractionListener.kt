package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun onPostLikeClicked(post: Post)
    fun onPostShareClicked(post: Post)
    fun onPostRemoveClicked(post: Post)
    fun onPostEditClicked(post: Post)
    fun onCancelEditClicked()

}