package dmitry.molchanov.fishingforecast

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/** Обертка для ArgumentCaptor для корректной работы с котлин кодом */
internal class CaptorWrapper<T>(private val captor: ArgumentCaptor<T>, private val obj: T) {

    val values: List<T>
        get() = captor().allValues

    fun capture(): T {
        captor.capture()
        return obj
    }

    fun value(): T = captor().value

    private fun captor(): ArgumentCaptor<T> = captor
}

internal fun <T> anyOfType(type: Class<T>): T = Mockito.any(type)