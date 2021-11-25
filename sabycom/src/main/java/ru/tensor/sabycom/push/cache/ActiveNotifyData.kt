package ru.tensor.sabycom.push.cache

import ru.tensor.sabycom.push.parser.data.PushType

/**
 * @author am.boldinov
 */
internal data class ActiveNotifyData(
    val tag: String,
    val id: Int,
    val type: PushType
)