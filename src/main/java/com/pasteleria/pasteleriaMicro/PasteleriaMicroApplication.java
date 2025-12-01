package com.pasteleria.pasteleriaMicro;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasteleriaMicroApplication {

	public static void main(String[] args) {
		// Cargar variables de entorno desde .env
		try {
			Dotenv dotenv = Dotenv.configure()
					.directory("./")
					.ignoreIfMissing()
					.load();
			
			// Establecer variables de entorno en el sistema
			if (dotenv != null) {
				dotenv.entries().forEach(entry -> {
					String key = entry.getKey();
					String value = entry.getValue();
					System.setProperty(key, value);
					System.out.println("Cargada variable: " + key + " = " + 
						(key.contains("PASSWORD") ? "****" : value));
				});
			}
		} catch (Exception e) {
			// Si no existe .env, continuar con variables del sistema
			System.out.println("⚠️ Archivo .env no encontrado, usando variables de entorno del sistema");
			System.out.println("Error: " + e.getMessage());
		}
		
		SpringApplication.run(PasteleriaMicroApplication.class, args);
	}

}
