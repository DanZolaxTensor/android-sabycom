package ru.tensor.sabycom.widget

import ru.tensor.sabycom.data.UserData

/**
 * Состояния виджета онлайн консультант
 *
 * @author ma.kolpakov
 */
internal sealed class WebWidgetState

/**
 * Виджет открылся
 * @param url ссылка на веб виджет
 * @param userData данные пользвателя
 */
internal class Opened(val url: String, val userData: UserData) : WebWidgetState()

/**
 * Виджет закрылся
 */
internal object Closed : WebWidgetState()
