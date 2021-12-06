package ru.tensor.sabycomdemo.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.widget.counter.UnreadCounterCallback

/**
 * Вью модель экрана с демонстрацией виджета "оналйн консультант"
 *
 * @author ma.kolpakov
 */
class DemoViewModel : ViewModel() {
    private val _messageCounter = MutableLiveData(0)
    val messageCounter: LiveData<Int> = _messageCounter

    init {
        Sabycom.unreadConversationCount { count -> _messageCounter.value = count }
    }

    override fun onCleared() {
        super.onCleared()
        Sabycom.unreadConversationCount(null)
    }
}