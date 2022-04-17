package com.yida.tinyleak

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.yida.tinyleaklib.leak.LifecycleDetector.activityLifeObserver

class MyApplication:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        activityLifeObserver(this,
            { activity ->
                val text="  ${activity.javaClass.simpleName}  发生内存泄漏啦！！！"
                showToast(text)
                Log.e("MyApplication", text) },
            { fragment ->
                val text="  ${fragment.javaClass.simpleName}  发生内存泄漏啦！！！"
                showToast(text)
                Log.e("MyApplication", text) },
            3
        )
    }


    fun showToast(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}