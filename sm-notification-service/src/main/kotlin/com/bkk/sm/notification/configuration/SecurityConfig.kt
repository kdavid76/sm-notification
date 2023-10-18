package com.bkk.sm.notification.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun configureSecurity(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
                .csrf { csrf -> csrf.disable() }
                .formLogin { formLogin -> formLogin.disable() }
                .httpBasic { httpBasic -> httpBasic.disable() }
                .authorizeExchange { exchanges ->
                    exchanges.pathMatchers(HttpMethod.POST, "/administrator/notification").permitAll()
                            .pathMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                            .anyExchange().denyAll()
                }
                .build()
    }
}
