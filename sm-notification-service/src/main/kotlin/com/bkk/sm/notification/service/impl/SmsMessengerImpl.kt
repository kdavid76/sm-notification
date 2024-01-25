package com.bkk.sm.notification.service.impl

import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.model.SmsParameters
import com.bkk.sm.notification.service.Messenger
import mu.KotlinLogging

class SmsMessengerImpl : Messenger {
    private val log = KotlinLogging.logger {}
    override fun sendMessage(notificationType: NotificationType, parameters: Map<String, String>) {
        log.info { "SMS text will be sent for typ=$notificationType to username=${parameters[SmsParameters.NAME.name]} at phone= ${parameters[SmsParameters.PHONENUMBER.name]}" }
    }
}