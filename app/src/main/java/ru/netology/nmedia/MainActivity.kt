package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.focusAndShowKeyboard
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.cancelButton.visibility = View.GONE
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)

        binding.PostsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.contentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                with(binding.contentEditText) {
                    if (text.toString().isBlank())
                        binding.cancelButton.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.cancelButton.visibility = View.VISIBLE
            }
        })

        binding.cancelButton.setOnClickListener {
            with(binding.contentEditText) {
                binding.cancelButton.visibility = View.GONE
                setText("")
                viewModel.onCancelEditClicked()
            }
        }

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
            }
            binding.cancelButton.visibility = View.GONE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.contentEditText) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    binding.cancelButton.visibility = View.VISIBLE
                    requestFocus()
                    focusAndShowKeyboard()
                } else {
                    binding.cancelButton.visibility = View.GONE
                    clearFocus()
                    hideKeyboard()
                }
            }
        }

        viewModel.shareEvent.observe(this) {
            //TODO пошарить
        }
    }
}





