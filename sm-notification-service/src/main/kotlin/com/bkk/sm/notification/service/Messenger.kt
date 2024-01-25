package com.bkk.sm.notification.service

import com.bkk.sm.common.kafka.notification.NotificationType

fun interface Messenger {
    fun sendMessage(notificationType: NotificationType, parameters: Map<String, String>)
}