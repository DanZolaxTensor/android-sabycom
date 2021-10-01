package ru.tensor.sabycom.push.parser

import ru.tensor.sabycom.push.parser.data.PushNotificationMessage

/**
 * @author am.boldinov
 */
internal class SabycomPushNotificationParser : PushNotificationParser {

    override fun isValidPayload(payload: Map<String, String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun parse(payload: Map<String, String>): PushNotificationMessage {
        TODO("Not yet implemented")
    }
}