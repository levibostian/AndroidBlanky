package com.app.service.type

typealias OrderedSet<E> = LinkedHashSet<E>

fun <E> orderedSet(vararg element: E): OrderedSet<E> = LinkedHashSet<E>(element.size).apply {
    addAll(element)
}
