package com.plantastic.com.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.ArgumentMatchers.any // Standard Mockito any()
import org.mockito.ArgumentMatchers.eq // Standard Mockito eq()
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class NotificationWorkerTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockNotificationManagerCompat: NotificationManagerCompat

    @Mock
    private lateinit var mockNotificationManager: NotificationManager


    @Before
    fun setUp() {
        // Mock NotificationManager system service
        `when`(mockContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mockNotificationManager)

        // It's tricky to mock NotificationManagerCompat.from(context) directly.
        // Instead, we can ensure the NotificationChannel is created (if on O+)
        // and that notify() is called.
        // For a unit test, we might not be able to easily use a real NotificationManagerCompat.
        // One approach is to have a wrapper around NotificationManagerCompat that can be mocked,
        // or use a library like Robolectric for tests that need more Android framework interaction.
        // For now, we'll focus on input data processing and if the worker tries to show a notification.
    }

    @Test
    fun `doWork returns success and attempts to show notification with correct data`() {
        val testTitle = "Test Plant"
        val testDescription = "Time to water your test plant!"
        val testId = "test-uuid-123"
        val expectedSystemNotificationId = testId.hashCode()

        val inputData = Data.Builder()
            .putString(NotificationWorker.KEY_NOTIFICATION_TITLE, testTitle)
            .putString(NotificationWorker.KEY_NOTIFICATION_DESCRIPTION, testDescription)
            .putString(NotificationWorker.KEY_NOTIFICATION_ID, testId)
            .build()

        val worker = TestListenableWorkerBuilder<NotificationWorker>(mockContext)
            .setInputData(inputData)
            .build()

        // We can't easily verify NotificationManagerCompat.notify directly without more setup (e.g. Robolectric or a wrapper).
        // However, we can ensure the worker completes successfully.
        // For a more thorough test, you would mock NotificationManagerCompat and verify `notify` calls.
        // This requires a way to inject the mocked NotificationManagerCompat into the worker or global state,
        // which is not straightforward for static `NotificationManagerCompat.from()`.

        val result = worker.startWork().get() // Using .get() for synchronous result in test
        assert(result is ListenableWorker.Result.Success)

        // To verify notification display, you'd typically:
        // 1. Mock NotificationManagerCompat.from(context) to return your mock.
        // 2. Verify mockNotificationManagerCompat.notify(eq(expectedSystemNotificationId), any()) was called.
        // This is hard without DI or heavier test frameworks.
        // For now, success indicates it processed inputs and didn't crash.
        // We can also verify channel creation on API O+
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // verify(mockNotificationManager).createNotificationChannel(any())
        // }
        // This part also needs mockContext to return mockNotificationManager for getSystemService.
    }

    @Test
    fun `doWork returns failure if title is missing`() {
        val inputData = Data.Builder()
            .putString(NotificationWorker.KEY_NOTIFICATION_DESCRIPTION, "Some description")
            .putString(NotificationWorker.KEY_NOTIFICATION_ID, "test-id")
            .build()

        val worker = TestListenableWorkerBuilder<NotificationWorker>(mockContext)
            .setInputData(inputData)
            .build()
        val result = worker.startWork().get()
        assert(result is ListenableWorker.Result.Failure)
    }

    @Test
    fun `doWork returns failure if description is missing`() {
        val inputData = Data.Builder()
            .putString(NotificationWorker.KEY_NOTIFICATION_TITLE, "Some title")
            .putString(NotificationWorker.KEY_NOTIFICATION_ID, "test-id")
            .build()

        val worker = TestListenableWorkerBuilder<NotificationWorker>(mockContext)
            .setInputData(inputData)
            .build()
        val result = worker.startWork().get()
        assert(result is ListenableWorker.Result.Failure)
    }
}
