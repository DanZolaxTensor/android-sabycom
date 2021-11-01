package ru.tensor.sabycom.widget.js

import android.webkit.JavascriptInterface
import androidx.annotation.WorkerThread
import org.json.JSONObject
import ru.tensor.sabycom.widget.counter.IUnreadCountController

internal class JSInterface(private val countController: IUnreadCountController) {
    var onCloseListener: OnJsCloseListener? = null

    @JavascriptInterface
    fun postMessage(data: String, targetOrigin: String) {
        val dataJson = JSONObject(data)
        when (dataJson.getString(ACTION)) {
            ACTION_CLOSE -> {
                onCloseListener?.onClose()
            }
            ACTION_UPDATE_UNREAD_MESSAGES -> {
                countController.updateCount(dataJson.getInt(VALUE))
            }
            else -> {
                //do nothing
            }
        }
        println(data)
    }

    companion object {
        const val ACTION_CLOSE = "toggleWindow"
        const val ACTION_UPDATE_UNREAD_MESSAGES = "unreadChange"
        const val ACTION = "action"
        const val VALUE = "value"
    }

    interface OnJsCloseListener {
        @WorkerThread
        fun onClose()
    }
}