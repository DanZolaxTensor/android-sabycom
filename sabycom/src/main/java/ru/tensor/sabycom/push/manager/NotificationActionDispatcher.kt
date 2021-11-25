package ru.tensor.sabycom.push.manager

/**
 * @author am.boldinov
 */
internal interface NotificationActionDispatcher {

    fun dispatchOnCancel(tag: String, id: Int)

    fun dispatchOnCancelAll()
}