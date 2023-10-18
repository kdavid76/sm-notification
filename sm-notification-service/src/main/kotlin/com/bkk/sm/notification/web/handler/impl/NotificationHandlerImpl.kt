package com.bkk.sm.notification.web.handler.impl

import com.bkk.sm.common.kafka.KafkaTopics
import com.bkk.sm.common.kafka.notification.Notification
import com.bkk.sm.notification.web.handler.NotificationHandler
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class NotificationHandlerImpl(
        private val notificationTemplate: KafkaTemplate<String, Notification>,
) : NotificationHandler {

    val log = KotlinLogging.logger {}
    override suspend fun postNotification(request: ServerRequest): ServerResponse {
        val notification = request.awaitBodyOrNull<Notification>()

        return notification?.let {
            log.info { "Message (id=${it.id}) of type=${it.type} found. Sending it to ${it.channel}" }
            notificationTemplate.send(KafkaTopics.NOTIFICATIONS.topicName, it)
            ServerResponse.ok().buildAndAwait()
        } ?: run {
            log.info { "Missing notification object, message can't be sent to Kafka topic." }
            ServerResponse.noContent().buildAndAwait()
        }
    }
}