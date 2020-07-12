package com.levibostian.extensions

fun Int.Companion.random(min: Int, max: Int): Int {
    return (min..max).random()
}

val Int.Companion.random: Int
    get() = Int.random(1, MAX_VALUE)