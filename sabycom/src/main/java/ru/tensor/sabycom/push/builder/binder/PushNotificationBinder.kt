package ru.tensor.sabycom.push.builder.binder

import ru.tensor.sabycom.push.builder.NotificationCompatBuilder
import ru.tensor.sabycom.push.builder.NotificationData

/**
 * @author am.boldinov
 */
internal interface PushNotificationBinder<DATA : NotificationData> :
    NotificationBinder<DATA, NotificationCompatBuilder>