package ru.tensor.sabycom.push.util

import androidx.lifecycle.*
import ru.tensor.sabycom.push.manager.NotificationLocker

/**
 * @author am.boldinov
 */
internal class DefaultNotificationLifecycleObserver(
    private val locker: NotificationLocker
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        locker.lock()
    }

    override fun onPause(owner: LifecycleOwner) {
        locker.unlock()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}

internal fun LifecycleOwner.attachNotificationLocker(locker: NotificationLocker) {
    lifecycle.addObserver(DefaultNotificationLifecycleObserver(locker))
}