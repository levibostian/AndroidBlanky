package com.app.service.pendingtasks

import com.levibostian.wendy.service.Wendy
import javax.inject.Inject

class PendingTasks @Inject constructor() {

//    fun addFooPendingTask(data: Foo) {
//        Wendy.shared.addTask(FooPendingTask(foo))
//    }

    fun runAllTasks() {
        Wendy.shared.runTasks(null)
    }
}
