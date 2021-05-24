package com.example.mp6

import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import com.mptsix.todaydiary.model.ServerRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun is_registerUser_works_well() {
        val mockUserRegisterRequest: UserRegisterRequest = UserRegisterRequest(
            userId = "kangdroid@outlook.com",
            userPassword = "testtest",
            userName = "test",
            userDateOfBirth = "2000",
            userPasswordAnswer = "",
            userPasswordQuestion = ""
        )

        runBlocking {
            val registerResponse: UserRegisterResponse = ServerRepository.registerUser(
                mockUserRegisterRequest
            )
            assertThat(registerResponse.registeredId).isEqualTo(mockUserRegisterRequest.userId)
        }
    }

    @Test
    fun is_loginRequest_works_well() {
        val mockUserRegisterRequest: UserRegisterRequest = UserRegisterRequest(
            userId = "kangdroid@outlook.com",
            userPassword = "testtest",
            userName = "test",
            userDateOfBirth = "2000",
            userPasswordAnswer = "",
            userPasswordQuestion = ""
        )

        runBlocking {
            val registerResponse: UserRegisterResponse = ServerRepository.registerUser(
                mockUserRegisterRequest
            )
            assertThat(registerResponse.registeredId).isEqualTo(mockUserRegisterRequest.userId)
        }

        val mockLoginRequest: LoginRequest = LoginRequest(
            userId = "kangdroid@outlook.com",
            userPassword = "testtest",
        )

        runBlocking {
            val loginResponse: LoginResponse = ServerRepository.loginRequest(
                mockLoginRequest
            )
            assertThat(loginResponse.userToken).isNotEqualTo("")
        }
    }
}