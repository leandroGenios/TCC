package br.com.meuspratos.ws;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {
        packages("br.com.meuspratos.resource");
    }
}