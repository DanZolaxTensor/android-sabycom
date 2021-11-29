package ru.tensor.sabycom.push.manager.app

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.core.view.doOnLayout
import ru.tensor.sabycom.R
import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * @author am.boldinov
 */
@UiThread
internal class OverlayViewController {

    private val animator = OverlayViewAnimator()

    fun show(activity: Activity, notification: SabycomNotification, animation: Boolean = true) {
        val binder = notification.inAppBinder ?: throw IllegalStateException()
        activity.tryGetRootView().postOnLayout {
            if (it.parent != null) {
                (it as ViewGroup).apply {
                    val view = findViewWithTag(notification.tag) ?: binder.create(context).apply {
                        tag = notification.tag
                        addView(this)
                        if (animation) {
                            animator.animateShow(this)
                        }
                    }
                    binder.bind(view, notification.data)
                }
            } else {
                Log.d("ViewController", "View detached, skip") // TODO message
            }
        }
    }

    fun hide(activity: Activity, tag: String, animation: Boolean = true) {
        activity.getRootView()?.apply {
            findViewWithTag<View>(tag)?.let { target ->
                val action = {
                    removeView(target)
                    if (childCount == 0) {
                        removeSelf()
                    }
                }
                if (animation) {
                    animator.animateHide(target) {
                        onFinish {
                            action()
                        }
                    }
                } else {
                    action()
                }
            }
        }
    }

    fun hideAll(activity: Activity) {
        activity.getRootView()?.apply {
            removeAllViews()
            removeSelf()
        }
    }

    private fun Activity.tryGetRootView(): ViewGroup {
        return getRootView() ?: run {
            val view = FrameLayout(this).apply {
                id = getRootViewId()
            }
            addContentView(
                view, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
                )
            )
            return view
        }
    }

    private fun Activity.getRootView(): ViewGroup? {
        return findViewById(getRootViewId())
    }

    private fun View.removeSelf() {
        (parent as? ViewGroup)?.removeView(this)
    }

    private fun getRootViewId(): Int {
        return R.id.sabycom_notification_overlay_root_view
    }

    private inline fun View.postOnLayout(crossinline action: (view: View) -> Unit) {
        doOnLayout {
            if (it.isInLayout) {
                it.post {
                    action(it)
                }
            } else {
                action(it)
            }
        }
    }
}