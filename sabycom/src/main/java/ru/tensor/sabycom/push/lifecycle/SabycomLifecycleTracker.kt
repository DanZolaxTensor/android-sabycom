package ru.tensor.sabycom.push.lifecycle

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.push.lifecycle.activity.ActivityState

/**
 * @author am.boldinov
 */
internal class SabycomLifecycleTracker(context: Context) : AppLifecycleTracker {

    private var numStarted = 0
    private var numCreated = 0
    private val foregroundState = MutableLiveData(false)
    private val activityState = MutableLiveData<ActivityState>()
    private var foregroundActivity: Activity? = null

    init {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            ForegroundListener()
        )
    }

    override fun isAppInForeground(): LiveData<Boolean> = foregroundState

    override fun isAppInForegroundNow(): Boolean = numStarted > 0

    override fun hasCreatedTasks(): Boolean = numCreated > 0

    override fun getForegroundActivity(): Activity? {
        return foregroundActivity
    }

    private inner class ForegroundListener : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            numCreated++
            activityState.value
        }

        override fun onActivityStarted(activity: Activity) {
            numStarted++
            if (numStarted == 1) {
                foregroundState.value = true
            }
        }

        override fun onActivityResumed(activity: Activity) {
            activityState.value = ActivityState.Resumed(activity)
            foregroundActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {
            activityState.value = ActivityState.Paused(activity)
            if (foregroundActivity == activity) {
                foregroundActivity = null
            }
        }

        override fun onActivityStopped(activity: Activity) {
            numStarted--
            if (numStarted == 0 && !activity.isChangingConfigurations) {
                foregroundState.value = false
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            numCreated--
            activityState.value = ActivityState.Destroyed(activity)

        }

    }
}