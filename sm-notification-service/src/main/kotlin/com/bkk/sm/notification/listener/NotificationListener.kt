package com.bkk.sm.notification.listener

import com.bkk.sm.common.kafka.notification.Notification
import com.bkk.sm.notification.factory.MessengerFactory
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "spring.kafka", name = ["bootstrap-servers"])
class NotificationListener(
    val factory: MessengerFactory
) {
    val log = KotlinLogging.logger {}

    @KafkaListener(topics = ["sm-notifications"], groupId = "notification")
    suspend fun consumeNotification(notification: Notification) {
        log.info { "Notification message accepted: $notification" }
        val messenger = factory.getMessenger(notification.type, notification.channel)

        messenger?.sendMessage(notification.type, notification.parameters) ?: run {
            log.info { "Cannot find messenger bean for type=${notification.type} and channel=${notification.channel}" }
        }
    }
}