package app.ice.readmanga.core.downloader

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val url = inputData.getStringArray("urls")?.toList() ?: return@withContext Result.failure()
                val fileName = inputData.getString("fileName") ?: return@withContext Result.failure()
                Downloader().downloadAsPdf(url, fileName)

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}