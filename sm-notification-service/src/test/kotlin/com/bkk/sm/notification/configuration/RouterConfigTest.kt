package com.bkk.sm.notification.configuration

import com.bkk.sm.common.kafka.notification.Notification
import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.web.handler.NotificationHandler
import com.ninjasquad.springmockk.MockkBean
import io.kotest.common.runBlocking
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import java.util.*

@WebFluxTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [RouterConfig::class, SecurityConfig::class])
@ActiveProfiles("test")
class RouterConfigTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var notificationHandler: NotificationHandler

    private val notification = Notification(id = UUID.randomUUID(), channel = NotificationChannel.EMAIL,
            type = NotificationType.USER_REGISTRATION_CONFIRM, parameters = mutableMapOf(
            Pair("key1", "value1"), Pair("key2", "value2")))

    @Test
    fun `Wrong URL for posting notification`() = runBlocking {

        webTestClient.post()
                .uri("/administrator/notificationx")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("API_VERSION", "V1")
                .body(BodyInserters.fromValue(notification))
                .exchange()
                .expectStatus().isUnauthorized

        coVerify(exactly = 0) { notificationHandler.postNotification(any()) }
    }

    @Test
    fun `Route to post a notification`() = runBlocking {

        coEvery { notificationHandler.postNotification(any()) } returns ServerResponse.ok().buildAndAwait()

        webTestClient.post()
                .uri("/administrator/notification")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("API_VERSION", "V1")
                .body(BodyInserters.fromValue(notification))
                .exchange()
                .expectStatus().isOk

        coVerify { notificationHandler.postNotification(any()) }
    }
}