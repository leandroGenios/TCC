package br.com.meuspratos.ws;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {
		register(new MyApplicationBinder());
        packages("br.com.meuspratos.resource");
    }
}