package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.longArg
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PreviewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val postId = arguments?.longArg

        val adapter = PostsAdapter(viewModel, isPreviewMode = true)
        binding.PostRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            val posts = viewModel.data.value?.filter { it.id == postId?.toULong() }

            if (posts != null) {
                adapter.submitList(posts)
            }
        }

        viewModel.shareEvent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.share_intent_title)
            )

            startActivity(shareIntent)
        }

        viewModel.navigatePostEdit.observe(viewLifecycleOwner) { content ->
            findNavController().navigate(
                R.id.action_previewPostFragment_to_newPostFragment,
                Bundle().apply {
                    textArg = content
                })
        }

        viewModel.navigatePostDelete.observe(viewLifecycleOwner) { _ ->
            findNavController().navigate(R.id.feedFragment)
        }

        return binding.root
    }
}