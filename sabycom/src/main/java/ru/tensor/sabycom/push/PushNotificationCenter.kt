package ru.tensor.sabycom.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import ru.tensor.sabycom.push.builder.chat.ChatNotificationBuilder
import ru.tensor.sabycom.push.builder.NotificationBuilder
import ru.tensor.sabycom.push.lifecycle.AppLifecycleTracker
import ru.tensor.sabycom.push.lifecycle.SabycomLifecycleTracker
import ru.tensor.sabycom.push.parser.PushNotificationParser
import ru.tensor.sabycom.push.parser.SabycomPushNotificationParser
import ru.tensor.sabycom.push.parser.data.PushType
import ru.tensor.sabycom.push.cache.ActiveNotificationCache
import ru.tensor.sabycom.push.cache.ActiveNotifyData
import ru.tensor.sabycom.push.cache.MemoryActiveNotificationCache
import ru.tensor.sabycom.push.manager.CompositeNotificationManager
import ru.tensor.sabycom.push.manager.NotificationActionDispatcher
import ru.tensor.sabycom.push.manager.app.InAppNotificationManager
import ru.tensor.sabycom.push.manager.push.PushNotificationManager
import ru.tensor.sabycom.push.util.NotificationChannelUtil
import ru.tensor.sabycom.widget.counter.IUnreadCountController
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author am.boldinov
 */
internal class PushNotificationCenter(
    private val context: Context,
    private val repository: Repository,
    private val countController: IUnreadCountController,
    private val parser: PushNotificationParser,
    private val activeStore: ActiveNotificationCache,
    lifecycleTracker: AppLifecycleTracker
) : SabycomPushService, NotificationActionDispatcher {

    constructor(
        context: Context,
        repository: Repository,
        countController: IUnreadCountController
    ) : this(
        context,
        repository,
        countController,
        SabycomPushNotificationParser(),
        MemoryActiveNotificationCache(),
        SabycomLifecycleTracker(context)
    )

    private val builderMap = mutableMapOf<PushType, NotificationBuilder>()
    private val handler = Handler(Looper.getMainLooper())
    private val notificationManager = CompositeNotificationManager(
        InAppNotificationManager(context, lifecycleTracker),
        PushNotificationManager(context)
    )

    init {
        initNotificationChannels()
        initPushBuilders()
    }

    override fun isSabycomPushNotification(payload: Map<String, String>): Boolean {
        return parser.isValidPayload(payload)
    }

    override fun handlePushNotification(payload: Map<String, String>) {
        countController.requestCount()
        val message = parser.parse(payload) // TODO check adressedId
        builderMap[message.type]?.build(message)?.let { notification ->
            handler.post {
                val notifyData = ActiveNotifyData(
                    notification.tag,
                    notification.id,
                    message.type
                )
                activeStore.add(notifyData)
                notificationManager.notify(notification)
            }
        }
    }

    override fun sendToken(token: String) {
        repository.sendPushToken(token)
    }

    override fun dispatchOnCancel(tag: String, id: Int) {
        notificationManager.cancel(tag, id)
    }

    override fun dispatchOnCancelAll() {
        notificationManager.cancelAll()
    }

    private fun initNotificationChannels() {
        NotificationChannelUtil.submitDefaultNotificationChannel(context, "САБИДОК") // TODO name
    }

    private fun initPushBuilders() {
        builderMap[PushType.CHAT] = ChatNotificationBuilder(this)
    }

}