package ru.tensor.sabycom.push.builder

import ru.tensor.sabycom.push.parser.data.PushCloudAction

/**
 * @author am.boldinov
 */
internal interface NotificationData {
    val tag: String
    val id: Int
    val title: String
    val text: String
    val action: PushCloudAction
}