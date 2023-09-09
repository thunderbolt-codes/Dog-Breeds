package dev.thunderbolt.dogbreeds.data.ext

import com.google.gson.Gson
import dev.thunderbolt.dogbreeds.data.remote.entity.ApiResponse
import retrofit2.Response

fun <T> Response<T>.parseResponse(): ApiResponse<*> {
    val body = body()
    val errorBody = errorBody()

    return when {
        body != null -> {
            ApiResponse.Success<T>(body)
        }

        errorBody != null -> {
            Gson().fromJson(errorBody.string(), ApiResponse.Error::class.java)
        }

        else -> {
            ApiResponse.Error("Unknown error", -1)
        }
    }
}
