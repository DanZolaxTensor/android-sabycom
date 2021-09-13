package ru.tenzor.sabycomdemo

import android.app.Application
import ru.tenzor.sabycom.Sabycom

/**
 * @author ma.kolpakov
 */
class SabycomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Sabycom.initialize("b956719f-965c-4204-815e-89894ba2aa9e")
    }
}