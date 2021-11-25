package ru.tensor.sabycom.push.builder

import android.app.Notification
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import ru.tensor.sabycom.push.util.NotificationChannelUtil
import ru.tensor.sabycom.push.util.SwipeOutHelper

/**
 * @author am.boldinov
 */
internal class NotificationCompatBuilder(
    val context: Context,
    category: String = DEFAULT_CATEGORY,
    channelId: String = NotificationChannelUtil.DEFAULT_NOTIFICATION_CHANNEL_ID
) : NotificationCompat.Builder(context, channelId) {

    init {
        setCategory(category)
        // design
        // setColor(ContextCompat.getColor(context)) TODO 30.09.2021 установить иконки виджета, либо использовать иконки приложения
        setSmallIcon(android.R.drawable.ic_dialog_info)

        // rules
        setAutoCancel(true)
        priority = NotificationCompat.PRIORITY_HIGH

        // lights
//        setLights(
//            ContextCompat.getColor(context, R.color.design_default_color_primary), // TODO 30.09.2021 цвет индикатора
//            LIGHT_ON_OFF_MS,
//            LIGHT_ON_OFF_MS
//        )

        //sound
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)?.let {
            setSound(it)
        }

        // actions
       // setDeleteIntent(SwipeOutHelper.createSwipeOutIntent(context, tag, id)) TODO
    }

    companion object {

        private const val DEFAULT_CATEGORY = Notification.CATEGORY_MESSAGE
        private const val LIGHT_ON_OFF_MS = 1000
    }
}