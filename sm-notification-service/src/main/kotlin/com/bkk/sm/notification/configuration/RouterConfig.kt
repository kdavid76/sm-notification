package com.bkk.sm.notification.configuration

import com.bkk.sm.notification.web.handler.NotificationHandler
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {

    private val log = KotlinLogging.logger {}

    @Bean
    fun administrativeRoutes(notificationHandler: NotificationHandler) = coRouter {
        before {
            log.info { "Processing User request from ${it.remoteAddress().orElse(null)} with headers=${it.headers()}" }
            it
        }

        "/administrator".nest {
            headers {
                it.header("API_VERSION")[0] == "V1"
            }.nest {
                contentType(MediaType.APPLICATION_JSON).nest {
                    POST("/notification", notificationHandler::postNotification)
                }
            }
        }
    }
}
