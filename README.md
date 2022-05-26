# tinyLeak是一款内存泄漏检测工具,请勿用于线上

- 他能监控activity和fragment的泄漏
- 和leakcanary对比，tinyLeak只做前期的泄漏检查功能，并不分析泄漏的引用关系，所以免去了dump内存的步骤
- leakcanary 需要dump内存，分析引用关系，所以没办法再线上使用
- leakcanary 需要dump内存，开发环境中有泄漏的情况下会一直dump内存，没办法先忽略再集中一次性修改，因为一直dump严重影响开发效率
- tinyLeak可以监控线上的内存泄漏情况
- 如果是线下开发环境，可以使用as的Profiler来分析泄漏原因

---
 

使用方法

-  Add it in your root build.gradle at the end of repositories:
```

allprojects {
		repositories {
			...
			maven { url 'http://192.168.31.241:8081/artifactory/meme_maven/' }
		}
	}
	
	
	
```

2 Add the dependency

```
implementation 'com.memezhibo.sdk:tinyleak:1.0.0'
```

3 在application里注册

kotlin 写法：
```

class MyApplication:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        activityLifeObserver(this,
            { activity -> Log.e("MyApplication", "  ${activity.javaClass.simpleName}  发生内存泄漏啦！！！") },
            { fragment -> Log.e("MyApplication", "${fragment.javaClass.simpleName}  发生内存泄漏啦！！！") },
            3
        )
    }


}
```

java写法：

```
LifecycleDetector.INSTANCE.activityLifeObserver(this, activity -> {
                    Log.e(TAG, "activity  " + activity.getClass().getSimpleName() + "  leaked");
                    return null;
                }
                ,
                fragment -> {
                    Log.e(TAG, "fragment  " + fragment.getClass().getSimpleName() + "  leaked");
                    return null;
                },
                5
        );
```

至此，就可以监控到activity和fragment的泄漏事件了


--- 

原理分析

```
    private var weakSet = Collections.newSetFromMap(WeakHashMap<Any, Boolean>())

```
- 使用一个弱应用的set存放Activity和Fragment
- 在Activity和Fragment oncreate时加入set
- 在Activity和Fragment ondestory后触发GC，此时弱引用set里如果还有已经destroy的Activity和Fragment，说明发生了内存泄漏。
