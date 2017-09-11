package com.levibostian.androidblanky.view.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.levibostian.androidblanky.view.ui.activity.AppCompatLifecycleActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

operator fun LifecycleCompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

class LifecycleCompositeDisposable private constructor() : LifecycleObserver {

    private val onStartStopComposite = CompositeDisposable()
    private val onPauseResumeComposite = CompositeDisposable()
    private val onCreateDestroyComposite = CompositeDisposable()

    private var lastLifecycleEvent: Lifecycle.Event? = null

    companion object {
        fun init(lifecycleOwner: LifecycleOwner): LifecycleCompositeDisposable {
            return init(lifecycleOwner.lifecycle)
        }

        fun init(lifecycleActivity: AppCompatLifecycleActivity): LifecycleCompositeDisposable {
            return init(lifecycleActivity.lifecycle)
        }

        private fun init(lifecycle: Lifecycle): LifecycleCompositeDisposable {
            val lifecycleCompositeDisposable = LifecycleCompositeDisposable()
            when (lifecycle.currentState) {
                Lifecycle.State.DESTROYED -> throw RuntimeException("You cannot create a ${LifecycleCompositeDisposable::class.java.simpleName} after the lifecycle is destroyed. Create one in onCreate()")
                Lifecycle.State.INITIALIZED -> {}
                Lifecycle.State.CREATED -> lifecycleCompositeDisposable.lastLifecycleEvent = Lifecycle.Event.ON_CREATE
                Lifecycle.State.STARTED,
                Lifecycle.State.RESUMED -> lifecycleCompositeDisposable.lastLifecycleEvent = Lifecycle.Event.ON_START
                null -> throw RuntimeException("You cannot create a ${LifecycleCompositeDisposable::class.java.simpleName} before the lifecycle is initialized. Create one in onCreate()")
            }
            lifecycle.addObserver(lifecycleCompositeDisposable)

            return lifecycleCompositeDisposable
        }
    }

    fun add(disposable: Disposable) {
        when (lastLifecycleEvent) {
            Lifecycle.Event.ON_CREATE -> onCreateDestroyComposite.add(disposable)
            Lifecycle.Event.ON_START -> onStartStopComposite.add(disposable)
            Lifecycle.Event.ON_RESUME -> onPauseResumeComposite.add(disposable)
            Lifecycle.Event.ON_PAUSE -> throw RuntimeException("You cannot add disposables after the lifecycle has paused.")
            Lifecycle.Event.ON_STOP -> throw RuntimeException("You cannot add disposables after the lifecycle has stopped.")
            Lifecycle.Event.ON_DESTROY -> throw RuntimeException("You cannot add disposables after the lifecycle has destroyed.")
            Lifecycle.Event.ON_ANY -> return
            null -> throw RuntimeException("You cannot add disposables until the lifecycle has been created.")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        lastLifecycleEvent = Lifecycle.Event.ON_START
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        lastLifecycleEvent = Lifecycle.Event.ON_STOP
        onStartStopComposite.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        lastLifecycleEvent = Lifecycle.Event.ON_RESUME
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        lastLifecycleEvent = Lifecycle.Event.ON_PAUSE
        onPauseResumeComposite.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        lastLifecycleEvent = Lifecycle.Event.ON_CREATE
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lastLifecycleEvent = Lifecycle.Event.ON_DESTROY
        onCreateDestroyComposite.clear()
    }

}