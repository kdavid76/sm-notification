package com.bkk.sm.notification.factory.impl

import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.factory.MessengerFactory
import com.bkk.sm.notification.service.Messenger
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "sm.notification", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class MessengerFactoryImpl : MessengerFactory {

    val log = KotlinLogging.logger {}

    val mapOfMessengerBeans = mutableMapOf<NotificationType, MutableMap<NotificationChannel, Messenger>>()
    override fun registerBean(
        notificationType: NotificationType,
        notificationChannel: NotificationChannel,
        messenger: Messenger
    ) {
        log.info { "Registering messenger bean for type=$notificationType and channel=$notificationChannel" }
        val map =
            mapOfMessengerBeans.computeIfAbsent(notificationType) { mutableMapOf() }

        map[notificationChannel] = messenger
    }

    override fun getMessenger(
        notificationType: NotificationType,
        notificationChannel: NotificationChannel
    ): Messenger? {
        return mapOfMessengerBeans[notificationType]?.get(notificationChannel)
    }
}