package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {
    //private val repository: PostRepository = InMemoryPostRepository()
//    private val repository: PostRepository = SharedPrefsPostRepository(application)
    private val repository: PostRepository = FilePostRepository(application)


    val data by repository::data
    val shareEvent by repository::shareEvent
    val currentPost by repository::currentPost

    val navigateToPostContentScreenEvent = SingleLiveEvent<Unit>()
    val playVideoURL = SingleLiveEvent<String?>()

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Some Author",
            content = content,
            published = System.currentTimeMillis(),
            likes = 0U,
            shares = 0U,
            views = 0U,
            likedByMe = false
        )
        repository.save(post)
        currentPost.value = null
    }

    fun viewed(post: Post) = repository.view(post.id)

    override fun onPostLikeClicked(post: Post) {
        repository.like(post.id)
    }

    override fun onPostShareClicked(post: Post) {
        //TODO необходимо увеличивать счетчик только по результаты вызова интента, не просто так
        repository.share(post.id)
    }

    override fun onPostRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onPostEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.call()
    }

    fun onAddPostClicked() {
        navigateToPostContentScreenEvent.call()
    }

    override fun onCancelEditClicked() {
        currentPost.value = null
    }

    override fun onPlayVideoClicked(post: Post) {
        playVideoURL.value = post.videoURL
    }
}