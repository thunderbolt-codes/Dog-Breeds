package dev.thunderbolt.dogbreeds.domain.entity

sealed class UIState<T> {

    abstract val data: T?

    data class Loading<T>(override val data: T? = null) : UIState<T>()
    data class Success<T>(override val data: T) : UIState<T>()
    data class Error<T>(val error: String, override val data: T? = null) : UIState<T>()
}
