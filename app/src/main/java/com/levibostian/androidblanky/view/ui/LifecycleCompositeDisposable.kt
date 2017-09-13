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

    private val composite = CompositeDisposable()

    companion object {
        fun init(lifecycleOwner: LifecycleOwner): LifecycleCompositeDisposable {
            return init(lifecycleOwner.lifecycle)
        }

        fun init(lifecycleActivity: AppCompatLifecycleActivity): LifecycleCompositeDisposable {
            return init(lifecycleActivity.lifecycle)
        }

        private fun init(lifecycle: Lifecycle): LifecycleCompositeDisposable {
            val lifecycleCompositeDisposable = LifecycleCompositeDisposable()

            lifecycle.addObserver(lifecycleCompositeDisposable)

            return lifecycleCompositeDisposable
        }
    }

    fun add(disposable: Disposable) {
        composite.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        composite.dispose()
    }

}