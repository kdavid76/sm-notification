package com.bkk.sm.notification.listener

import com.bkk.sm.common.kafka.notification.Notification
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class NotificationListener {
    val log = KotlinLogging.logger {}

    @KafkaListener(topics = ["sm-notifications"], groupId = "notification")
    fun consumeNotification(notification: Notification) {
        log.info { "Notification message accepted: $notification" }
    }
}