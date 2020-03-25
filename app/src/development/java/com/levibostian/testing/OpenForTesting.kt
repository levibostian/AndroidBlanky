package com.levibostian.testing

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * release builds. In release/beta builds, we will disable the [OpenForTesting] annotation but by default enable it.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds used for testing.
 */
@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting