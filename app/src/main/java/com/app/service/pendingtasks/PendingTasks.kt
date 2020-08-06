package com.app.service.pendingtasks

import android.content.Context
import com.app.Env
import com.app.extensions.isDevelopment
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.service.Wendy

// Using an interface as it's easier to mock instead of using an open class. You may forget to declare the class and method as open.
interface PendingTasks {
    fun initPendingTasks()
    fun delete()
    fun deleteAsync(done: () -> Unit?)
    fun runAllTasks()
}

/**
 * Wrapper around Wendy. We don't want Wendy to be executed during UI tests, for example, so we wrap it.
 */
class WendyPendingTasks constructor(
    private val context: Context,
    private val pendingTasksFactory: PendingTasksFactory
) : PendingTasks {

    override fun initPendingTasks() {
        Wendy.init(context, pendingTasksFactory)
        WendyConfig.debug = Env.isDevelopment
    }

    override fun delete() {
        Wendy.shared.clear()
    }

    override fun deleteAsync(done: () -> Unit?) {
        Wendy.shared.clearAsync(done)
    }

//    fun addFooPendingTask(data: Foo) {
//        Wendy.shared.addTask(FooPendingTask(foo))
//    }

    override fun runAllTasks() {
        Wendy.shared.runTasks(null)
    }
}
