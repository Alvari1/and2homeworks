package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityShareBinding

class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent ?: return
        if (intent.action != Intent.ACTION_SEND) return

        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (text.isNullOrBlank()) {
            Snackbar.make(
                binding.root,
                getString(R.string.share_intent_empty_text_error),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(android.R.string.ok) {
                finish()
                setResult(Activity.RESULT_CANCELED, intent)
            }.show()
        } else {
            binding.root.text = text
            setResult(Activity.RESULT_OK, intent)
        }
    }
}