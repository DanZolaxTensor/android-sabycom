package ru.tensor.sabycom.push.lifecycle.activity

import android.app.Activity

/**
 * @author am.boldinov
 */
internal sealed class ActivityState(val host: Activity) {

    class Resumed(host: Activity) : ActivityState(host)

    class Paused(host: Activity) : ActivityState(host)

    class Started(host: Activity) : ActivityState(host)

    class Stopped(host: Activity) : ActivityState(host)

    class Created(host: Activity) : ActivityState(host)

    class Destroyed(host: Activity) : ActivityState(host)
}