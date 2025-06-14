package com.plantastic.com.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.plantastic.com.workers.NotificationWorker
import java.util.concurrent.TimeUnit

class NotificationRepository {

    private val gson = Gson()
    private val prefsName = "notification_prefs"
    private val notificationsKey = "notifications_list"

    fun saveNotification(context: Context, notification: Notification) {
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val existingNotifications = getNotifications(context).toMutableList()
        existingNotifications.add(notification)
        val jsonNotifications = gson.toJson(existingNotifications)
        sharedPrefs.edit().putString(notificationsKey, jsonNotifications).apply()
    }

    fun getNotifications(context: Context): List<Notification> {
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val jsonNotifications = sharedPrefs.getString(notificationsKey, null)
        return if (jsonNotifications != null) {
            val type = object : TypeToken<List<Notification>>() {}.type
            gson.fromJson(jsonNotifications, type)
        } else {
            emptyList()
        }
    }

    fun clearAllNotifications(context: Context) { // Optional: for testing or reset
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(notificationsKey).apply()
        // Optionally, also cancel all work manager jobs if you have a way to iterate them
        // Or if they are all tagged with a common prefix, etc.
        // For now, this just clears SharedPreferences.
    }

    fun scheduleNotification(context: Context, notification: Notification) {
        val workManager = WorkManager.getInstance(context)

        val inputData = Data.Builder()
            .putString(NotificationWorker.KEY_NOTIFICATION_TITLE, notification.title)
            .putString(NotificationWorker.KEY_NOTIFICATION_DESCRIPTION, notification.description)
            .putString(NotificationWorker.KEY_NOTIFICATION_ID, notification.id)
            .build()

        // Create a PeriodicWorkRequest
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = notification.frequency.toLong(),
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInputData(inputData)
            .addTag(notification.id) // Use notification.id as a tag for cancellation
            .build()

        // Enqueue the work request, keeping existing work if it's already scheduled for this ID
        workManager.enqueueUniquePeriodicWork(
            notification.id, // Unique work name, can be same as tag
            ExistingPeriodicWorkPolicy.KEEP, // Or REPLACE if you want to update existing
            workRequest
        )
    }

    fun cancelNotification(context: Context, notification: Notification) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(notification.id) // Cancel by tag (notification.id)
        // Also remove from SharedPreferences
        removeNotificationFromPrefs(context, notification)
    }

    private fun removeNotificationFromPrefs(context: Context, notificationToRemove: Notification) {
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val existingNotifications = getNotifications(context).toMutableList()
        if (existingNotifications.removeAll { it.id == notificationToRemove.id }) {
            val jsonNotifications = gson.toJson(existingNotifications)
            sharedPrefs.edit().putString(notificationsKey, jsonNotifications).apply()
        }
    }
}
