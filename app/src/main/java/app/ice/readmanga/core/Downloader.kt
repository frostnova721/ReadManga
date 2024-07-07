package app.ice.readmanga.core

import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import androidx.compose.ui.geometry.Rect
import app.ice.readmanga.utils.client
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import okhttp3.OkHttpClient
import okhttp3.Request
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

    public suspend fun downloadAsPdf(urls: List<String>) {
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
        }

        writeToStorage(pdfDocument)

    }

    private fun writeToStorage(pdf: PdfDocument) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val filePath = "$dir/manga.pdf"
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