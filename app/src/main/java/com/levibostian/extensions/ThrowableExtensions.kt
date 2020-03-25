package com.levibostian.extensions

import java.io.PrintWriter
import java.io.StringWriter

// Source https://stackoverflow.com/a/1149712/1486374
fun Throwable.getStacktrace(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    printStackTrace(pw)

    return sw.toString()
}