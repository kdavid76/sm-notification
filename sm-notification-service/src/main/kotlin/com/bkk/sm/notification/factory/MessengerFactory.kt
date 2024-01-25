package com.bkk.sm.notification.factory

import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.service.Messenger

interface MessengerFactory {

    fun registerBean(notificationType: NotificationType, notificationChannel: NotificationChannel, messenger: Messenger)

    fun getMessenger(notificationType: NotificationType, notificationChannel: NotificationChannel): Messenger?
}