package ru.tensor.sabycom.push.builder.binder

import android.view.View
import ru.tensor.sabycom.push.builder.NotificationData

/**
 * @author am.boldinov
 */
internal interface InAppNotificationBinder<DATA : NotificationData, V : View> :
    NotificationBinder<DATA, V>