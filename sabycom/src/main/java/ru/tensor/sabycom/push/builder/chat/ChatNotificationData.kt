package ru.tensor.sabycom.push.builder.chat

import ru.tensor.sabycom.push.builder.NotificationData
import ru.tensor.sabycom.push.parser.data.PushCloudAction

/**
 * @author am.boldinov
 */
internal class ChatNotificationData(
    override val tag: String,
    override val id: Int,
    override val title: String,
    override val text: String,
    override val action: PushCloudAction,
    val avatarUrl: String? = null,
    val unreadCount: Int
) : NotificationData