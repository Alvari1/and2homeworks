package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post (
            id = 1U,
            author = "Alex",
            content = getString(R.string.sample_text),
            published = System.currentTimeMillis(),
            likes = 0U,
            shares = 0U,
            views = 1999U,
            likedByMe = false
        )

        binding.render(post)

        binding.likePic?.setOnClickListener {
            post.likes = if(post.likedByMe) {
                if (post.likes.equals(0U)) 0U else post.likes - 1U
            } else {
                post.likes + 1U
            }
            post.likedByMe = !post.likedByMe

            binding.likePic?.setImageResource(getLikeIconResId(post.likedByMe))
            binding.likesCount?.text = getFormattedCounter(post.likes)

            //TODO заглушка на просмотры, убать при реализаии просмотра постов из списка
            post.views = post.views + 1U
            binding.viewsCount?.text = getFormattedCounter(post.views)
        }

        binding.sharesPic?.setOnClickListener {
            post.shares = post.shares + 1U

            binding.sharesCount?.text = getFormattedCounter(post.shares)

            //TODO заглушка на просмотры, убать при реализаии просмотра постов из списка
            post.views = post.views + 1U
            binding.viewsCount?.text = getFormattedCounter(post.views)
        }
    }

    private fun PostListItemBinding.render(post : Post) {
        authorName.text = post.author
        date.text = getFormattedDate(post.published)
        postText.text = post.content
        likesCount.text = getFormattedCounter(post.likes)
        sharesCount.text = getFormattedCounter(post.shares)
        viewsCount.text = getFormattedCounter(post.views)
        likePic?.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId (liked: Boolean) =
        if (liked) R.drawable.ic_favorite_active_24dp else R.drawable.ic_favorite_border_24dp


    private fun getFormattedCounter(amount: ULong)  = when (amount) {
            in 0U..999U -> amount.toString()
            in 1000U..1099U -> (amount/1000U).toString().plus("K")
            in 1100U..999999U -> {
                val temp = amount/100U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("K")
                else (temp/10U).toString().plus("K")
            }
            in 1000000UL..999999999UL -> {
                val temp = amount/100000U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("M")
                else (temp/10U).toString().plus("M")
            }
            else -> {
                val temp = amount/100000000U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("B")
                else (temp/10U).toString().plus("B")
            }
        }

    private fun getFormattedDate(timestamp: Long)  =
        run { SimpleDateFormat("dd.MM.yyyy HH:mm").format(timestamp).toString() }
}





