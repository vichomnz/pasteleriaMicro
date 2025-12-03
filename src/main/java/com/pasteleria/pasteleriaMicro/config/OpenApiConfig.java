package com.pasteleria.pasteleriaMicro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        // Servidor local
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor Local");
        
        // Servidor Railway (puedes cambiar la URL cuando despliegues)
        Server productionServer = new Server();
        productionServer.setUrl("https://tu-proyecto.railway.app");
        productionServer.setDescription("Servidor Producción");
        
        // Información de contacto
        Contact contact = new Contact();
        contact.setName("Equipo Pastelería Micro");
        contact.setEmail("contacto@pasteleriamicro.cl");
        
        // Licencia
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
        
        // Información de la API
        Info info = new Info()
                .title("Pastelería Micro API")
                .version("1.0.0")
                .description("API REST para gestión de pastelería con productos, pedidos, usuarios y autenticación JWT")
                .contact(contact)
                .license(license);
        
        // Esquema de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("JWT Authentication")
                .description("Ingresa el token JWT obtenido del endpoint /api/auth/login");
        
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", securityScheme));
    }
}
