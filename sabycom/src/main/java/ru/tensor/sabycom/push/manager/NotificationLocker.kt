package ru.tensor.sabycom.push.manager

/**
 * @author am.boldinov
 */
internal interface NotificationLocker {

    fun lock()

    fun unlock()
}