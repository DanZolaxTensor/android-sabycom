package ru.tensor.sabycom.push.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.tensor.sabycom.push.receiver.SwipeOutNotificationReceiver

/**
 * Утилита для работы со свайпом по уведомлению
 *
 * @author am.boldinov
 */
internal object SwipeOutHelper {

    private const val INTENT_DATA_FORMAT = "content://sabycom/push/%s/%d"
    private const val INTENT_MIME_TYPE = "sabycom.android.mime.type/sabycom.push.notification"

    private const val NOTIFY_TAG_FOR_SWIPE_OUT_KEY = "NOTIFY_TAG_FOR_SWIPE_OUT_KEY"
    private const val NOTIFY_ID_FOR_SWIPE_OUT_KEY = "NOTIFY_ID_FOR_SWIPE_OUT"

    /**
     * Создает интент на обработку события по удалению уведомления
     * @see [androidx.core.app.NotificationCompat.Builder.setDeleteIntent]
     *
     * @param context контекст приложения
     * @param tag тег, под которым было опубликовано уведомление
     * @param notifyId идентификатор, под которым было опубликовано уведомление
     * @see [ru.tensor.sabycom.push.manager.NotificationManager]
     */
    fun createSwipeOutIntent(context: Context, tag: String, notifyId: Int): PendingIntent {
        val uri = Uri.parse(String.format(INTENT_DATA_FORMAT, tag, notifyId))
        val intent = Intent(context, SwipeOutNotificationReceiver::class.java).apply {
            putExtra(NOTIFY_TAG_FOR_SWIPE_OUT_KEY, tag)
            putExtra(NOTIFY_ID_FOR_SWIPE_OUT_KEY, notifyId)
            setDataAndType(uri, INTENT_MIME_TYPE)
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Возвращает тег, под которым было опубликовано уведомление.
     * Можно использовать в момент обработки события удаления.
     */
    fun getSwipeOutTag(intent: Intent): String? {
        return intent.getStringExtra(NOTIFY_TAG_FOR_SWIPE_OUT_KEY)
    }

    /**
     * Возвращает идентификатор, под которым было опубликовано ранее уведомление.
     * Можно использовать в момент обработки события удаления.
     */
    fun getSwipeOutId(intent: Intent): Int {
        return intent.getIntExtra(NOTIFY_ID_FOR_SWIPE_OUT_KEY, -1)
    }
}