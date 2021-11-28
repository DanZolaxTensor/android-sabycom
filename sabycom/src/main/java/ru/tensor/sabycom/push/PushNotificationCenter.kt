package ru.tensor.sabycom.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import ru.tensor.sabycom.push.builder.chat.ChatNotificationBuilder
import ru.tensor.sabycom.push.builder.NotificationBuilder
import ru.tensor.sabycom.push.parser.PushNotificationParser
import ru.tensor.sabycom.push.parser.SabycomPushNotificationParser
import ru.tensor.sabycom.push.parser.data.PushType
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
    private val parser: PushNotificationParser
) : SabycomPushService, NotificationActionDispatcher {

    constructor(
        context: Context,
        repository: Repository,
        countController: IUnreadCountController
    ) : this(
        context,
        repository,
        countController,
        SabycomPushNotificationParser()
    )

    private val builderMap = mutableMapOf<PushType, NotificationBuilder>()
    private val handler = Handler(Looper.getMainLooper())
    private val notificationManager = CompositeNotificationManager(
        InAppNotificationManager(context),
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
        val message = parser.parse(payload)
        if (message.addresseeId == repository.getUserData()?.id.toString()) {
            countController.requestCount()
            builderMap[message.type]?.build(message)?.let { notification ->
                handler.post {
                    notificationManager.notify(notification)
                }
            }
        } else {
            Log.d("SKIP","SKIP")
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