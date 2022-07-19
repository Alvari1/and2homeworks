package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Like
import ru.netology.nmedia.dto.NewPost
import ru.netology.nmedia.enum.Action
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val serializedAction = data[Action.KEY] ?: return
        val action = Action.values().find { it.key == serializedAction } ?: return

        when (action) {
            Action.LIKE -> handleLike(data[CONTENT_KEY] ?: return)
            Action.NEW_POST -> handleNewPost(data[CONTENT_KEY] ?: return)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", token)
    }

    private fun handleNewPost(serializedContent: String) {
        val newPostContext = gson.fromJson(serializedContent, NewPost::class.java)
        val title = getString(
            R.string.notification_user_newpost,
            newPostContext.userName
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(newPostContext.postContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(newPostContext.postContent)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleLike(serializedContent: String) {
        val likeContext = gson.fromJson(serializedContent, Like::class.java)
        val title = getString(
            R.string.notification_user_liked,
            likeContext.userName,
            likeContext.postAuthor,
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    companion object {
        const val CONTENT_KEY = "content"
        const val CHANNEL_ID = "remote"
    }
}