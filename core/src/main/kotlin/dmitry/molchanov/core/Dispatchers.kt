package dmitry.molchanov.core

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

@JvmInline
value class DispatcherDefault(
    private val coroutineContext: CoroutineContext = Dispatchers.Default
) : CoroutineContext by coroutineContext

@JvmInline
value class DispatcherIo(
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineContext by coroutineContext