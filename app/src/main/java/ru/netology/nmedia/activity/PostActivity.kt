package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.setText(intent.getStringExtra(Intent.EXTRA_TEXT))

        binding.cancelButton.visibility = if (!binding.edit.text.isNullOrBlank())
            View.VISIBLE
        else
            View.GONE

        binding.edit.requestFocus()
        setResult(Activity.RESULT_OK, intent)

        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text
            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(POST_CONTENT_EXTRA_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
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
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private companion object {
        const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, PostActivity::class.java)
                .putExtra(Intent.EXTRA_TEXT, input)

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode !== Activity.RESULT_OK) return null
            intent ?: return null

            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }
}