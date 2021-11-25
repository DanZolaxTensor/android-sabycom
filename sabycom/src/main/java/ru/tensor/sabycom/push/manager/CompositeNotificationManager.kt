package ru.tensor.sabycom.push.manager

import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * @author am.boldinov
 */
internal class CompositeNotificationManager(
    vararg managers: NotificationManager
) : NotificationManager {

    private val sources = managers

    override fun notify(notification: SabycomNotification): Boolean {
        return sources.find {
            it.notify(notification)
        }?.let { notify ->
            sources.forEach {
                if (it != notify) {
                    it.cancel(notification.tag, notification.id)
                }
            }
            true
        } ?: false
    }

    override fun cancel(tag: String, id: Int) {
        sources.forEach {
            it.cancel(tag, id)
        }
    }

    override fun cancelAll() {
        sources.forEach {
            it.cancelAll()
        }
    }
}