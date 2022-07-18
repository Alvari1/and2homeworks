package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryDao(
    private val dao: PostDao,
) : PostRepository {
    override val currentPost = MutableLiveData<Post?>(null)

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun like(postId: Long) {
        dao.like(postId)
    }

    override fun share(postId: Long) {
        dao.share(postId)
    }

    override fun view(postId: Long) {
        dao.view(postId)
    }

    override fun delete(postId: Long) {
        dao.delete(postId)
    }
}