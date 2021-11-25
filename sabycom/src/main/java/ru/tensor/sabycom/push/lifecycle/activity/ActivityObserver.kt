package ru.tensor.sabycom.push.lifecycle.activity

import android.app.Activity

/**
 * @author am.boldinov
 */
internal interface ActivityObserver {

    fun onStateChanged(activityState: ActivityState)

    fun onCleared(activity: Activity)
}