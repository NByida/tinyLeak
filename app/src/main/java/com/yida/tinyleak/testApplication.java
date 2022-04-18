package com.yida.tinyleak;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;

import com.yida.tinyleaklib.leak.LifecycleDetector;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class testApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LifecycleDetector.INSTANCE.activityLifeObserver(this, fragment -> null,
                fragment -> null,
                7
        );
    }
}
