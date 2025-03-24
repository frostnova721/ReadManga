package app.ice.readmanga.core.downloader

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import app.ice.readmanga.utils.client
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import java.io.File
import java.io.FileOutputStream

class Downloader {

    private suspend fun getImage(url: String): ByteArray? {
        val res = client.get(url)
        if(res.status != HttpStatusCode.OK) {
            Log.e("E-DOWN", "Couldn't download the image!")
            throw Exception("CANT DOWNLOAD THIS IMAGE")
//            return null;
        };
        println("Download success")
        val imageData = res.readBytes()

        return imageData;
    }

    suspend fun startDownload(urls: List<String>, fileName: String, context: Context) {
        val downloadData = Data.Builder()
            .putStringArray("urls", urls.toTypedArray())
            .putString("fileName", fileName)
            .build()

        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(downloadData)
            .build()

        WorkManager.getInstance(context).enqueue(downloadWorkRequest)
    }

    suspend fun downloadAsPdf(urls: List<String>, fileName: String, onProgress: (Int) -> Unit ) {
//        val imageArray: MutableList<ByteArray> = mutableListOf<ByteArray>()
        val pdfDocument = PdfDocument()

        //write to pdf
        for((index, url) in urls.withIndex()) {
            val image = this.getImage(url) ?: return
//            imageArray.add(image)
            val bmp = BitmapFactory.decodeByteArray(image, 0, image.size)
            val pageDetails = PdfDocument.PageInfo.Builder(bmp.width, bmp.height, index + 1).create()
            val page = pdfDocument.startPage(pageDetails)
            val canvas = page.canvas
            canvas.drawBitmap(bmp, 0f, 0f, null)
            pdfDocument.finishPage(page)
            val progress = (index + 1) * 100 / urls.size
            onProgress(progress)
        }
        val regex = "[^a-zA-Z0-9-\\s]".toRegex()
        writeToStorage(pdfDocument, fileName.replace(regex, ""))

    }

    private fun writeToStorage(pdf: PdfDocument, fileName: String) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val filePath = "$dir/$fileName.pdf"
        val file = File(filePath)

        try {
            pdf.writeTo(FileOutputStream(file))
            pdf.close()
            Log.i("DOWN", "Download complete!")
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }
}