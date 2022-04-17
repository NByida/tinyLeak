package com.yida.tinyleaklib.leak

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

object LifecycleDetector {

    fun activityLifeObserver(
        application: Application,
        activityLeaked: (any: Activity) -> Unit,
        fragmentLeaked: (any: Fragment) -> Unit,
        destroyCheckNumbs: Int = 5
    ) {
        application.registerActivityLifecycleCallbacks(ActivityRefWatcher(onActivityCreated = { activity, _ ->
            LeakDetector.addActivityOrFragmentOnCreate(activity)
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                    FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentDestroyed(
                        fm: FragmentManager,
                        f: Fragment
                    ) {
                        super.onFragmentDestroyed(fm, f)
                        LeakDetector.checkLeak(f)
                    }

                    override fun onFragmentCreated(
                        fm: FragmentManager,
                        f: androidx.fragment.app.Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentCreated(fm, f, savedInstanceState)
                        LeakDetector.addActivityOrFragmentOnCreate(f)
                    }
                }, true)
            }
        }, {
            LeakDetector.checkLeak(it)
        }
        ))
        LeakDetector.activityLeaked = activityLeaked
        LeakDetector.fragmentLeaked = fragmentLeaked
        LeakDetector.destoryCheckNums = destroyCheckNumbs
    }

}

