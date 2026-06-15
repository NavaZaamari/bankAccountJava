package com.example.bank;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.utils.SpringDocUtils;

@Configuration
public class SwaggerSecurityConfig {

    static {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(
                org.springframework.security.core.userdetails.UserDetails.class,
                org.springframework.security.core.GrantedAuthority.class
        );
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerToken";
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
