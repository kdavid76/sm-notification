package com.bkk.sm.notification.factory.impl

import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.service.impl.EmailMessengerImpl
import com.bkk.sm.notification.service.impl.SmsMessengerImpl
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.Test

class MessengerFactoryImplTest {

    private val factory = MessengerFactoryImpl()

    @Test
    fun `Register and get beans from factory`() {
        val smsMessengerImpl = mockk<SmsMessengerImpl>()
        val emailMessengerImpl = mockk<EmailMessengerImpl>()

        factory.registerBean(NotificationType.USER_REGISTRATION_CONFIRM, NotificationChannel.EMAIL, emailMessengerImpl)
        factory.registerBean(NotificationType.USER_REGISTRATION_CONFIRM, NotificationChannel.SMS, smsMessengerImpl)

        factory.getMessenger(
            NotificationType.USER_REGISTRATION_CONFIRM,
            NotificationChannel.SMS
        ) shouldBe smsMessengerImpl
        factory.getMessenger(
            NotificationType.USER_REGISTRATION_CONFIRM,
            NotificationChannel.EMAIL
        ) shouldBe emailMessengerImpl

        factory.getMessenger(NotificationType.COMPANY_REGISTRATION_CONFIRM, NotificationChannel.SMS) shouldBe null
        factory.getMessenger(NotificationType.COMPANY_REGISTRATION_CONFIRM, NotificationChannel.EMAIL) shouldBe null
    }
}