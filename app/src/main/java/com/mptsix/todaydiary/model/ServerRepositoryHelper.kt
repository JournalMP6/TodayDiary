package com.mptsix.todaydiary.model

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

object ServerRepositoryHelper {
    private val logTag: String = this::class.java.simpleName
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun <T> executeServer(apiFunction: Call<T>): T {
        val response: Response<T> = exchangeDataWithServer(apiFunction)

        if (!response.isSuccessful) {
            handleDataError(response)
        }

        return response.body()
            ?: throw NoSuchFieldException("Response was OK, but wrong response body received!")
    }

    fun handle204(toExecute: () -> ResponseBody) {
        runCatching {
            toExecute()
        }.onFailure {
            Log.w(this::class.java.simpleName, "Usually current function returns 204")
            Log.w(this::class.java.simpleName, "This is usually normal, since ServerRepositoryHelper throws an exception if they do not have body.")
            Log.w(this::class.java.simpleName, "Programmatically, this exception is marked as ignored, but in case this exception was REAL Error.")
            Log.w(this::class.java.simpleName, "Ignore this warning if following stacktrace says \"Response was OK, but wrong response body received!\"")
            Log.e(this::class.java.simpleName, it.stackTraceToString())
        }
    }

    /**
     * T exchangeDataWithServer(apiFunction: Call<T>): Response<T>
     * Exchange data with server.
     *
     * apiFunction: APIInterface function to execute
     * returns: Response of data.
     * Throws: Exception when network failed.
     */
    private fun <T> exchangeDataWithServer(apiFunction: Call<T>): Response<T> {
        return runCatching {
            apiFunction.execute()
        }.getOrElse {
            Log.e(logTag, "Error when getting root token from server.")
            Log.e(logTag, it.stackTraceToString())
            throw it
        }
    }

    /**
     * fun <reified T> handleDataError(response: Response<T>)
     *
     * handle response error if needed[i.e error response]
     */
    private fun <T> handleDataError(response: Response<T>) {
        // If error body is null, something went wrong.
        val errorBody: ResponseBody = response.errorBody()!!

        // Get error body as map, since spring's default error response was sent.
        val errorBodyMap: Map<String, String> = objectMapper.readValue(errorBody.string()) // Could throw
        if (errorBodyMap.contains("errorMessage")) { // Common about our error response and spring error response
            throw RuntimeException("Server responded with: ${errorBodyMap["errorMessage"]}")
        } else {
            throw NoSuchFieldException("Error message was not found!! This should be reported to developers.")
        }
    }

}