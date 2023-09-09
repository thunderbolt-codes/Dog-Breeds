package dev.thunderbolt.dogbreeds.data.remote.entity

sealed class ApiResponse<T> {

    data class Success<T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Error(
        val message: String,
        val code: Int,
    ) : ApiResponse<String>() {
        val errMessage: String
            get() = "$code: $message"
    }
}
