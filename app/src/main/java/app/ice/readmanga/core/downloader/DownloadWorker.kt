package app.ice.readmanga.core.downloader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.ice.readmanga.MainActivity
import app.ice.readmanga.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class DownloadWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val notificationChannelId = "readmangadownloadchannel"

    private val notificationId = System.currentTimeMillis();

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val url = inputData.getStringArray("urls")?.toList()
                    ?: return@withContext Result.failure()
                val fileName =
                    inputData.getString("fileName") ?: return@withContext Result.failure()

                val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
                val notificationBuilder = createNotificationBuilder()

                Downloader().downloadAsPdf(url, fileName) { progress ->
                    setProgressAsync(workDataOf("progress" to progress))
                    notificationBuilder.setProgress(100, progress, false)
                    notificationManager?.notify(notificationId.toInt(), notificationBuilder.build())
                }

                notificationManager?.cancel(notificationId.toInt())
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        createChannel()
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        var pending by Delegates.notNull<Int>()
        pending = PendingIntent.FLAG_IMMUTABLE

        val mainActivityPendingIntent =
            PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, pending)

        return NotificationCompat.Builder(
            applicationContext, notificationChannelId
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading")
            .setSilent(true)
            .setOngoing(true)
            .setProgress(100, 0, true)
            .setContentIntent(mainActivityPendingIntent)

    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(0, createNotificationBuilder().build())
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nchannel = NotificationChannel(
                this.notificationChannelId,
                "ReadManga Downloader",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nmgr: NotificationManager? =
                ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)

            nmgr?.createNotificationChannel(nchannel)
        }
    }
}