package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener,
    private val isPreviewMode: Boolean = false
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: PostItemBinding,
        listener: PostInteractionListener,
        isPreviewMode: Boolean = false
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onPostRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onPostEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likePic.setOnClickListener { listener.onPostLikeClicked(post) }
            binding.sharesPic.setOnClickListener { listener.onPostShareClicked(post) }

            binding.options.setOnClickListener { popupMenu.show() }

            binding.playButton.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }

            binding.video.setOnClickListener {
                listener.onPlayVideoClicked(post)
            }

            if (!isPreviewMode) {
                binding.postItem.setOnClickListener {
                    listener.onPostItemClicked(post)
                }
            }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding)
            {
                authorName.text = post.author
                date.text = getFormattedDate(post.published)
                postText.text = post.content

                likePic.isChecked = post.likedByMe
                likePic.text = getFormattedCounter(post.likes)

                sharesPic.text = getFormattedCounter(post.shares)
                viewsPic.text = getFormattedCounter(post.views)

                videoGroup.visibility =
                    if (post.videoURL.isNullOrBlank()) View.GONE else View.VISIBLE
            }
        }

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
        val binding = PostItemBinding.inflate(inflator, parent, false)

        return ViewHolder(
            binding = binding,
            listener = interactionListener,
            isPreviewMode = isPreviewMode
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