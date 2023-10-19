package com.bkk.sm.notification.web.handler.impl

import com.bkk.sm.common.kafka.KafkaTopics
import com.bkk.sm.common.kafka.notification.Notification
import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.CompletableFuture

@ActiveProfiles("test")
class KafkaNotificationHandlerImplTest {
    private val kafkaTemplate = mockk<KafkaTemplate<String, Notification>>()

    private val handler = KafkaNotificationHandlerImpl(kafkaTemplate)

    private val notification = Notification(id = UUID.randomUUID(), channel = NotificationChannel.EMAIL,
            type = NotificationType.USER_REGISTRATION_CONFIRM, parameters = mutableMapOf(
            Pair("key1", "value1"), Pair("key2", "value2")))

    @BeforeEach
    fun initMocks() {
        clearMocks(kafkaTemplate)
    }

    @Test
    fun `Post notification`(): Unit = runBlocking {
        // given
        val request = MockServerRequest.builder().body(Mono.just(notification))
        coEvery { kafkaTemplate.send(eq(KafkaTopics.NOTIFICATIONS.topicName), any()) } returns mockk<CompletableFuture<SendResult<String, Notification>>>()

        // when
        val response = handler.postNotification(request)

        // then
        coVerify { kafkaTemplate.send(eq(KafkaTopics.NOTIFICATIONS.topicName), any()) }
        response.statusCode() shouldBe HttpStatus.OK
    }
}