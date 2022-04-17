package com.yida.tinyleaklib.leak

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import com.yida.tinyleaklib.BuildConfig
import java.util.*
import kotlin.collections.ArrayList


object LeakDetector {
    var TAG = "LeakDetector"


    private var weakSet = Collections.newSetFromMap(WeakHashMap<Any, Boolean>())

    fun addActivityOrFragmentOnCreate(any: Any) {
        weakSet.add(any)
    }


    private var destoryedActivityOrFramgment = ArrayList<Int>()

    private fun runGc() {
        // Code taken from AOSP FinalizationTest:
        // https://android.googlesource.com/platform/libc ore/+/master/support/src/test/java/libcore/
        // java/lang/ref/FinalizationTester.java
        // System.gc() does not garbage collect every time. Runtime.gc() is
        // more likely to perform a gc.
        Runtime.getRuntime().gc()
        enqueueReferences()
        System.runFinalization()
    }

    private fun enqueueReferences() {
        // Hack. We don't have a programmatic way to wait for the reference queue daemon to move
        // references to the appropriate queues.
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            throw AssertionError()
        }
    }


    private fun checkActivity() {
        num = 0
        runGc()
        if (weakSet.isNotEmpty()) {
            val iterator = weakSet.iterator()
            while (iterator.hasNext()) {
                val any = iterator.next()
                if (any != null) {
                    if (destoryedActivityOrFramgment.contains(any.hashCode())) {
                        Log.e(TAG, " ${any.javaClass.simpleName}  (hashcode:${any.hashCode()}) leaked after destroy invoked")
                        if (any is Activity) {
                            activityLeaked.invoke(any)
                        } else if (any is Fragment) {
                            fragmentLeaked.invoke(any)
                        }
                        iterator.remove()
                    }
                    destoryedActivityOrFramgment.remove(any.hashCode())
                }
            }
        }
    }

    var activityLeaked={ any: Activity->}
    var fragmentLeaked={ any: Fragment->}



    var destoryCheckNums = 0

    var num = 0

    fun checkLeak(any: Any) {
        destoryedActivityOrFramgment.add(any.hashCode())
        if (num++ >= destoryCheckNums || BuildConfig.DEBUG) {
            Log.d("LeakDetector", "checkLeak")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Looper.getMainLooper().queue.addIdleHandler {
                    checkActivity()
                    false
                }
            } else {
                myHandler.post {
                    checkActivity()
                }
            }
        }
    }

    var myHandler = Handler(Looper.myLooper()!!)

}