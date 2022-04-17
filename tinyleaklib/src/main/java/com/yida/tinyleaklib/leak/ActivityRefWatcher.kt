package com.yida.tinyleaklib.leak

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityRefWatcher(
    var onActivityCreated: (activity: Activity, savedInstanceState: Bundle?) -> Unit,
    var onActivityDestroyed: (activity: Activity) -> Unit
) : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        onActivityCreated.invoke(activity, savedInstanceState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        onActivityDestroyed.invoke(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }


}