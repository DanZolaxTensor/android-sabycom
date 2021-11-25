package ru.tensor.sabycom.push.cache

import ru.tensor.sabycom.push.parser.data.PushType

/**
 * @author am.boldinov
 */
internal class MemoryActiveNotificationCache : ActiveNotificationCache {

    private val cache = mutableListOf<ActiveNotifyData>()

    @Synchronized
    override fun add(data: ActiveNotifyData) {
        if (!cache.contains(data)) {
            cache.add(data)
        }
    }

    @Synchronized
    override fun remove(type: PushType) {
        cache.removeAll { it.type == type }
    }

    @Synchronized
    override fun removeAll() {
        cache.clear()
    }

    @Synchronized
    override fun get(type: PushType): List<ActiveNotifyData> {
        return cache.filter { it.type == type }
    }
}