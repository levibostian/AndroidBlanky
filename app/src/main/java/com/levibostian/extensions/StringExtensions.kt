package com.levibostian.extensions

fun String.Companion.random(length: Int): String {
    val possibleChars = ('a'..'z').toList().toTypedArray()
    return (1..length).map { possibleChars.random() }.joinToString("")
}

val String.Companion.random: String
    get() = random(15)

// Thanks, https://stackoverflow.com/a/60010299/1486374
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    return camelRegex.replace(this) {
        "_${it.value}"
    }.toLowerCase()
}
fun String.snakeToCamelCase(): String {
    val snakeRegex = "_[a-zA-Z]".toRegex()

    return snakeRegex.replace(this) {
        it.value.replace("_","")
                .toUpperCase()
    }
}