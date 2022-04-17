package com.yida.tinyleak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    var yidahandler: Handler? = null
    var testLeak = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        yidahandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                findViewById<TextView>(R.id.tv)
                Log.d("LeakDetector", "handleMessage leak")
            }
        }

        if (testLeak) {
            yidahandler?.postDelayed({
                Log.d("LeakDetector", "just4test leak")
            }, 100000)
        }

        findViewById<TextView>(R.id.tv).setOnClickListener {
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            this@MainActivity.startActivity(intent)
            finish()
        }
        findViewById<TextView>(R.id.add).setOnClickListener {
            val dialogFragment1 = DialogFragment1()
            dialogFragment1.show(supportFragmentManager, System.currentTimeMillis().toString())
        }
    }


}