package ru.tensor.sabycom.widget

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SabycomActivityViewModel(application) as T
    }
}