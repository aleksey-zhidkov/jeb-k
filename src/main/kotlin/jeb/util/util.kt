package jeb.util

sealed abstract class Try<out R> {

    class Success<out R>(override val result: R) : Try<R>()

    class Failure<out R>(val reason: Throwable) : Try<R>() {
        override val result: R
            get() = throw reason
    }

    abstract val result: R

}