package ru.tensor.sabycom.push.cache

import ru.tensor.sabycom.push.parser.data.PushType

/**
 * @author am.boldinov
 */
internal interface ActiveNotificationCache {

    fun add(data: ActiveNotifyData)

    fun remove(type: PushType)

    fun removeAll()

    fun get(type: PushType): List<ActiveNotifyData>
}