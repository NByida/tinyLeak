package com.yida.tinyleak

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment

class DialogFragment1: DialogFragment() {

    var yidahandler: Handler? = null
    var testLeak = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView = TextView(context)
        textView.width = 1000
        textView.height = 1000
        textView.gravity = Gravity.CENTER
        textView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        textView.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        textView.text = "我是DialogFragment:1"
        Log.e("yida", "onCreateView")

        yidahandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                textView.text
                Log.d("LeakDetector", "handleMessage leak")
            }
        }

        if (testLeak) {
            yidahandler?.postDelayed({
                Log.d("LeakDetector", "just4test leak")
            }, 100000)
        }

        return textView
    }
}