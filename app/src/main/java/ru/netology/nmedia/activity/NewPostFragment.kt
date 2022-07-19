package ru.netology.nmedia.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.longArg: Long by LongArg
        private const val BACKED_UP_TEXT = "backedUpText"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val preferences =
            context?.getSharedPreferences("newpost", Context.MODE_PRIVATE)

        arguments?.textArg
            ?.let(binding.edit::setText)

        if (binding.edit.text.isNullOrBlank()) binding.edit.setText(
            preferences?.getString(
                BACKED_UP_TEXT,
                null
            )
        )

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val text = binding.edit.text
            if (!text.isNullOrBlank()) viewModel.onSaveButtonClicked(text.toString())

            preferences?.edit()?.clear()?.apply()

            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val typedText = binding.edit.text?.toString()
            if (!typedText.isNullOrBlank())
                preferences?.edit()?.putString(BACKED_UP_TEXT, typedText)?.apply()
            else preferences?.edit()?.clear()?.apply()

            findNavController().popBackStack()
        }

        return binding.root
    }
}