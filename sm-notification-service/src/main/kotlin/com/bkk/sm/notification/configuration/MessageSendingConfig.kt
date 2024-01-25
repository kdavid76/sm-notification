package com.bkk.sm.notification.configuration

import com.bkk.sm.common.kafka.notification.NotificationChannel
import com.bkk.sm.common.kafka.notification.NotificationType
import com.bkk.sm.notification.factory.MessengerFactory
import com.bkk.sm.notification.service.Messenger
import com.bkk.sm.notification.service.impl.EmailMessengerImpl
import com.bkk.sm.notification.service.impl.SmsMessengerImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.nio.charset.StandardCharsets


@Configuration
@ConditionalOnProperty(prefix = "sm.notification", name = ["enabled"], havingValue = "true")
class MessageSendingConfig(
    val messengerFactory: MessengerFactory
) {

    @Bean
    @Qualifier("sms")
    @ConditionalOnProperty(
        prefix = "sm.notification",
        name = ["sms.enabled"],
        havingValue = "true"
    )
    fun smsMessenger(): Messenger {
        val messenger = SmsMessengerImpl()

        messengerFactory.registerBean(NotificationType.COMPANY_REGISTRATION_CONFIRM, NotificationChannel.SMS, messenger)
        messengerFactory.registerBean(NotificationType.USER_REGISTRATION_CONFIRM, NotificationChannel.SMS, messenger)
        return messenger
    }

    @Bean
    @Qualifier("email")
    @ConditionalOnProperty(
        prefix = "sm.notification",
        name = ["email.enabled"],
        havingValue = "true"
    )
    fun emailMessenger(): Messenger {
        val messenger = EmailMessengerImpl()

        messengerFactory.registerBean(
            NotificationType.COMPANY_REGISTRATION_CONFIRM,
            NotificationChannel.EMAIL,
            messenger
        )
        messengerFactory.registerBean(NotificationType.USER_REGISTRATION_CONFIRM, NotificationChannel.EMAIL, messenger)
        return messenger
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "sm.notification",
        name = ["email.enabled"],
        havingValue = "true"
    )
    fun springTemplateEngine(
        htmlTemplateResolver: ClassLoaderTemplateResolver,
        messageSource: MessageSource
    ): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(htmlTemplateResolver)
        templateEngine.setTemplateEngineMessageSource(messageSource)
        return templateEngine
    }

    @Bean
    @ConditionalOnProperty(
        prefix = "sm.notification",
        name = ["email.enabled"],
        havingValue = "true"
    )
    fun htmlTemplateResolver(): ClassLoaderTemplateResolver {
        val emailTemplateResolver = ClassLoaderTemplateResolver()
        emailTemplateResolver.prefix = "/templates/"
        emailTemplateResolver.suffix = ".html"
        emailTemplateResolver.templateMode = TemplateMode.HTML
        emailTemplateResolver.characterEncoding = StandardCharsets.UTF_8.name()
        return emailTemplateResolver
    }

    @Bean
    fun emailMessageSource(): ResourceBundleMessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("mailMessages")
        return messageSource
    }
}