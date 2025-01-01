package com.lsdconsulting.stub.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.lsdconsulting.stub.integration.model.GreetingRequest
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.commons.lang3.RandomStringUtils.secure
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import kotlin.random.Random

const val GET_CONTROLLER_URL = "http://localhost:8099/getController"
const val POST_CONTROLLER_URL = "http://localhost:8099/postController"
const val PUT_CONTROLLER_URL = "http://localhost:8099/putController"
const val DELETE_CONTROLLER_URL = "http://localhost:8099/deleteController"
const val MULTIPLE_METHOD_CONTROLLER_URL = "http://localhost:8099/multipleController"

open class BaseRestControllerIT {
    val restTemplate = RestTemplate()

    val name: String = secure().nextAlphabetic(10)
    val paramLong: Long = Random.nextLong()
    val paramInt: Int = Random.nextInt()
    val param: String = secure().nextAlphabetic(10)
    val param1: String = secure().nextAlphabetic(10)
    val param2: String = secure().nextAlphabetic(10)
    val param3: String = secure().nextAlphabetic(10)
    val param4: String = secure().nextAlphabetic(10)
    val paramSet: Set<String> = setOf(param1, param2, param3, param4)
    val paramList: List<String> = listOf(param2, param3)
    val customResponseBody: String = secure().nextAlphabetic(10)
    val httpStatus: HttpStatus = HttpStatus.valueOf(Random.nextInt(500, 511))

    val greetingResponse = GreetingResponse(name = name)
    val greetingRequest = GreetingRequest(name = name)

    @BeforeEach
    fun setup() = WireMock.reset()

    companion object {
        private var wireMockServer: WireMockServer = WireMockServer(WireMockConfiguration.options().port(8099))

        @JvmStatic
        @BeforeAll
        internal fun setupAll() {
            if (!wireMockServer.isRunning) {
                wireMockServer.start()
            }
            WireMock.configureFor(wireMockServer.port())
        }
    }
}
