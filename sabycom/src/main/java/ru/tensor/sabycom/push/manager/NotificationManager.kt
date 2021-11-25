package ru.tensor.sabycom.push.manager

import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * @author am.boldinov
 */
internal interface NotificationManager {

    fun notify(notification: SabycomNotification): Boolean

    fun cancel(tag: String, id: Int)

    fun cancelAll()
}