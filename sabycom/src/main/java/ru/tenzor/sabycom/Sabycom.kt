package ru.tenzor.sabycom

import androidx.appcompat.app.AppCompatActivity
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycom.widget.SabycomController

/**
 * @author ma.kolpakov
 */
object Sabycom {
    //region widget

    private var sabycomController: SabycomController? = null

    fun initialize(apiKey: String) {
        sabycomController = SabycomController(apiKey)
    }

    fun preLoad() {
        checkNotNull(sabycomController) { NOT_INIT_ERROR }.preload()
    }

    fun show(activity: AppCompatActivity) {
        checkNotNull(sabycomController) { NOT_INIT_ERROR }.show(activity)
    }

    fun hide() {
        checkNotNull(sabycomController) { NOT_INIT_ERROR }.hide()
    }

    fun registerUser(userData: UserData) {
        checkNotNull(sabycomController) { NOT_INIT_ERROR }.registerUser(userData)
        sabycomController
    }

    fun subscribeToUnreadMessage(callback: (Int) -> Unit) {}

    //endregion

    //region push notification

    fun isSabycomPushNotification() {}
    fun handlePushNotification() {}

    //endregion

}

private const val NOT_INIT_ERROR =
    "Before using Sabycom, it is necessary to initialize in the Application class [Sabycom.initialize(<API key>)]"