package com.plantastic.com.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.plantastic.com.R

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val KEY_NOTIFICATION_TITLE = "notification_title"
        const val KEY_NOTIFICATION_DESCRIPTION = "notification_description"
        const val KEY_NOTIFICATION_ID = "notification_id" // Used for the system notification ID
        private const val CHANNEL_ID = "plantastic_notifications_channel"
    }

    override fun doWork(): Result {
        val title = inputData.getString(KEY_NOTIFICATION_TITLE)
        val description = inputData.getString(KEY_NOTIFICATION_DESCRIPTION)
        // The notificationId for NotificationManager can be a hash of the unique notification ID string
        // or convert a portion of the UUID string to an Int.
        // For simplicity, using a fixed ID for now, but this should be unique per notification.
        val systemNotificationId = inputData.getString(KEY_NOTIFICATION_ID)?.hashCode() ?: 1


        if (title.isNullOrEmpty() || description.isNullOrEmpty()) {
            return Result.failure()
        }

        createNotificationChannel()
        showNotification(title, description, systemNotificationId)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Plantastic Reminders"
            val descriptionText = "Channel for Plantastic plant care reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, description: String, notificationId: Int) {
        // It's good practice to check for POST_NOTIFICATIONS permission before trying to display a notification,
        // though WorkManager itself will often handle this gracefully or log an error.
        // For this exercise, we'll assume permission is granted, as it's handled elsewhere.

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(applicationContext).notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // This can happen if POST_NOTIFICATIONS permission is missing.
            // Log error or handle accordingly.
            return // Result.failure() could be returned here as well.
        }
    }
}
