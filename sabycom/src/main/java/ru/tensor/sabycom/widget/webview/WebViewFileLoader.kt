package ru.tensor.sabycom.widget.webview

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import im.delight.android.webview.AdvancedWebView

/**
 * @author ma.kolpakov
 */
internal class WebViewFileLoader(private val contextProvider: () -> Context?) : AdvancedWebView.Listener,
    ActivityResultCallback<Boolean> {

    private var fileUrl: String? = null
    private var fileName: String? = null
    var permissionLauncher: ActivityResultLauncher<String>?=null

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        contextProvider.invoke()?.let { context ->
            if (!checkPermission(context)) {
                permissionLauncher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                downloadFile(context, url, suggestedFilename)
            }
        }
    }

    override fun onActivityResult(isGranted: Boolean) {
        contextProvider.invoke()?.let { context ->
            if (isGranted || checkPermission(context)) {
                downloadFile(context, fileUrl, fileName)
            } else {
                Toast.makeText(context, "No permission to save file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) = Unit

    override fun onPageFinished(url: String?) = Unit

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) = Unit

    override fun onExternalPageRequest(url: String?) = Unit

    /**
     * Проверка разрешения на запись во внутреннее хранилище
     */
    private fun checkPermission(context: Context) = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Запускает загрузку файла через DOWNLOAD_SERVICE
     */
    private fun downloadFile(context: Context, url: String?, suggestedFilename: String?) {
        if (url.isNullOrEmpty()) return

        val request = DownloadManager.Request(Uri.parse(url))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, suggestedFilename)

        request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }
}
