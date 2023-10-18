package com.bkk.sm.notification.web.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

fun interface NotificationHandler {
    suspend fun postNotification(request: ServerRequest): ServerResponse
}