package ru.tenzor.sabycomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tenzor.sabycom.Sabycom

/**
 * @author ma.kolpakov
 */
class DemoViewModel : ViewModel() {
    private val _messageCounter = MutableLiveData(0)
    val messageCounter: LiveData<Int> = _messageCounter

    init {
        Sabycom.unreadConversationCount {
            _messageCounter.value = it
        }
    }
}