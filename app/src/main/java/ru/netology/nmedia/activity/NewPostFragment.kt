package ru.netology.nmedia.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.longArg: Long by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.textArg
            ?.let(binding.edit::setText)

        binding.edit.requestFocus()

        binding.cancelButton.visibility = if (!binding.edit.text.isNullOrBlank())
            View.VISIBLE
        else
            View.GONE

        binding.ok.setOnClickListener {
            val text = binding.edit.text
            if (!text.isNullOrBlank()) viewModel.onSaveButtonClicked(text.toString())

            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                with(binding.edit) {
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
            binding.cancelButton.visibility = View.GONE
            findNavController().navigateUp()
        }

        return binding.root
    }
}