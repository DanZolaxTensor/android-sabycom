package ru.tensor.sabycom.push.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.tensor.sabycom.push.util.SwipeOutHelper

/**
 * Приемник события об удалении пользователем уведомления
 *
 * @author am.boldinov
 */
internal class SwipeOutNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val tag = SwipeOutHelper.getSwipeOutTag(intent)
        val id = SwipeOutHelper.getSwipeOutId(intent)
    }
}