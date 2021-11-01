package ru.tensor.sabycom.widget.counter

/**
 * Коллбек обновления непрочитанных сообщений
 *
 * @author ma.kolpakov
 */
interface UnreadCounterCallback {
    /**@SelfDocumented**/
    fun updateCount(count: Int)
}