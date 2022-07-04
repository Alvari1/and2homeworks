package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat

typealias OnPostLikeClicked = (Post) -> Unit
typealias OnPostShareClicked = (Post) -> Unit

internal class PostsAdapter(
    private val onLikeClicked: OnPostLikeClicked,
    private val onShareClicked: OnPostShareClicked
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: PostListItemBinding,
        onLikeClicked: OnPostLikeClicked,
        onShareClicked: OnPostShareClicked

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likePic.setOnClickListener { onLikeClicked(post) }
            binding.sharesPic.setOnClickListener { onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding)
            {
                authorName.text = post.author
                date.text = getFormattedDate(post.published)
                postText.text = post.content
                likesCount.text = getFormattedCounter(post.likes)
                sharesCount.text = getFormattedCounter(post.shares)
                viewsCount.text = getFormattedCounter(post.views)
                likePic.setImageResource(getLikeIconResId(post.likedByMe))
            }
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_favorite_active_24dp else R.drawable.ic_favorite_border_24dp


        private fun getFormattedCounter(amount: ULong) = when (amount) {
            in 0U..999U -> amount.toString()
            in 1000U..1099U -> (amount / 1000U).toString().plus("K")
            in 1100U..999999U -> {
                val temp = amount / 100U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("K")
                else (temp / 10U).toString().plus("K")
            }
            in 1000000UL..999999999UL -> {
                val temp = amount / 100000U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("M")
                else (temp / 10U).toString().plus("M")
            }
            else -> {
                val temp = amount / 100000000U
                if (temp.toDouble() % 10 > 0)
                    temp.toDouble().div(10).toString().plus("B")
                else (temp / 10U).toString().plus("B")
            }
        }

        private fun getFormattedDate(timestamp: Long) =
            run { SimpleDateFormat("dd.MM.yyyy HH:mm").format(timestamp).toString() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflator, parent, false)
        return ViewHolder(
            binding = binding,
            onLikeClicked = onLikeClicked,
            onShareClicked = onShareClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}