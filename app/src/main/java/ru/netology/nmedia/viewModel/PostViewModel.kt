package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.PostRepositoryDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {
    private val repository: PostRepository = PostRepositoryDao(
        AppDb.getInstance(context = application).postDao()
    )

    val data = repository.getAll()
    val currentPost by repository::currentPost

    val playVideoURL = SingleLiveEvent<String?>()
    val navigatePostEdit = SingleLiveEvent<String?>()
    val navigatePostShare = SingleLiveEvent<String?>()
    val navigatePostItem = SingleLiveEvent<Long>()
    val navigatePostDelete = SingleLiveEvent<Long>()

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Some Author",
            content = content,
            published = System.currentTimeMillis(),
            likes = 0,
            shares = 0,
            views = 0,
            likedByMe = false,
            url = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onPostLikeClicked(post: Post) {
        repository.like(post.id)
    }

    override fun onPostShareClicked(post: Post) {
        //TODO необходимо увеличивать счетчик только по результаты вызова интента, не просто так
        repository.share(post.id)
        navigatePostShare.value = post?.content
    }

    override fun onPostRemoveClicked(post: Post) {
        repository.delete(post.id)
        navigatePostDelete.value = post?.id
    }

    override fun onPostEditClicked(post: Post) {
        currentPost.value = post
        navigatePostEdit.value = post?.content
    }

    override fun onCancelEditClicked() {
        currentPost.value = null
    }

    override fun onPlayVideoClicked(post: Post) {
        playVideoURL.value = post.url
    }

    override fun onPostItemClicked(post: Post) {
        repository.view(post.id)
        navigatePostItem.value = post?.id
    }
}