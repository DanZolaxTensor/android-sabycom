package ru.tensor.sabycom.push.builder.binder

import android.content.Context

/**
 * @author am.boldinov
 */
internal interface NotificationBinder<DATA, NOTIFICATION> {

    fun create(context: Context): NOTIFICATION

    fun bind(view: NOTIFICATION, data: DATA)
}