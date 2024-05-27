package com.example.hello.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
@EnableWebFlux
class WebConfig : WebFluxConfigurer{


    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
//            .allowedHeaders("Authorization", "Content-Type")
//            .exposedHeaders("Custom-Header")
//            .allowCredentials(true)
//            .maxAge(3600);

        // Add more mappings...
    }

}