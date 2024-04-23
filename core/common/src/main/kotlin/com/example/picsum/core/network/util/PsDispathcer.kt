package com.example.picsum.core.network.util

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val psDispatcher: PsDispatchers)

enum class PsDispatchers {
    Default,
    IO,
}
