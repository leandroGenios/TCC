package br.com.meuspratos.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ingrediente")
@Produces(MediaType.APPLICATION_JSON)
public class IngredienteResource {
	
	@GET
	@Path("getInfo/{codigo}")
	public Response getInfo(@PathParam("codigo") int codigo) {
		return Response.ok().build();
	}
}
