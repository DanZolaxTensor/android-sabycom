package ru.tenzor.sabycom

import androidx.appcompat.app.AppCompatActivity
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycom.widget.SabycomFeature

/**
 * СБИС онлайн консультант.
 * @author ma.kolpakov
 */
object Sabycom {
    //region widget

    private var sabycomFeature: SabycomFeature? = null

    /**
     * Инициализация компонента предпочтительно вызывать в onCreate вашего Application класса
     * @param apiKey - API Ключ приложения
     */
    fun initialize(apiKey: String) {
        sabycomFeature = SabycomFeature(apiKey)
    }

    /**
     * Добавить информацию о пользователе. Метод должен быть вызван до [show]. Метод необходимо вызывать
     * даже если нет информации о пользователе, в таком случае необходимо передать только идентификатор
     * пользователя [UserData]
     */
    fun registerUser(userData: UserData) {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.registerUser(userData)
        sabycomFeature
    }

    /**
     * Показать веб виджет
     * @throws IllegalStateException - если метод был вызван до [initialize] или пользователь не был зарегистрирован методом [registerUser]
     */
    fun show(activity: AppCompatActivity) {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.show(activity)
    }

    /**
     * Скрыть веб виджет
     */
    fun hide() {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.hide()
    }

    /**
     * Запросить количество непрочитанных сообщений асинхронно.
     * @param callback обратный вызов с результатом запроса
     */
    fun unreadConversationCount(callback: (Int) -> Unit) {
        // TODO: 14.09.2021 реализовать запрос количества непрочитанных сообщений https://online.sbis.ru/opendoc.html?guid=3966a770-62ae-4965-a6aa-732aea72b57c
    }

    //endregion

    //region push notification

    fun isSabycomPushNotification() {
        // TODO: 14.09.2021
    }

    fun handlePushNotification() {
        // TODO: 14.09.2021
    }

    //endregion

}

private const val NOT_INIT_ERROR =
    "Before using Sabycom, it is necessary to initialize in the Application class [Sabycom.initialize(<API key>)]"