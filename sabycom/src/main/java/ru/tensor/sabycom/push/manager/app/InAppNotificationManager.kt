package ru.tensor.sabycom.push.manager.app

import android.app.Dialog
import android.content.Context
import ru.tensor.sabycom.push.builder.SabycomNotification
import ru.tensor.sabycom.push.lifecycle.AppLifecycleTracker
import ru.tensor.sabycom.push.manager.NotificationManager
import java.util.*

/**
 * @author am.boldinov
 */
internal class InAppNotificationManager(
    context: Context,
    private val lifecycleTracker: AppLifecycleTracker
) : NotificationManager {

    private val dialogMap = WeakHashMap<Dialog, NotifyData>()
    private val viewController = OverlayViewController()

    override fun notify(notification: SabycomNotification): Boolean {
        return lifecycleTracker.getForegroundActivity()?.let {
            viewController.show(it, notification)
            true
        } ?: false
//        cancel(tag, id)
//        activityProvider.getActivity()?.let {
//            dialogMap[buildDialog(it).apply {
//                show()
//            }] = NotifyData(tag, id)
//            if (it is ComponentActivity) {
//                it.lifecycle.addObserver(object : DefaultLifecycleObserver {
//                    override fun onDestroy(owner: LifecycleOwner) {
//                        cancel(tag, id)
//                        owner.lifecycle.removeObserver(this)
//                    }
//                })
//            }
//        } ?: throw IllegalStateException()
//        activityProvider.getActivity()?.let {
//            Observable
//        }
    }

    override fun cancel(tag: String, id: Int) {
        lifecycleTracker.getForegroundActivity()?.let {
            viewController.hide(it, tag)
        }
//        dialogMap.iterator().apply {
//            while (hasNext()) {
//                next().apply {
//                    if (value.tag == tag && value.id == id) {
//                        key.dismiss()
//                        remove()
//                    }
//                }
//            }
//        }
    }

    override fun cancelAll() {
        // TODO
    }

    private data class NotifyData(val tag: String, val id: Int)
}