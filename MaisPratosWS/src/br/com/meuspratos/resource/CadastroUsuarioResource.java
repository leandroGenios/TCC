package br.com.meuspratos.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.meuspratos.model.CadastroUsuario;

@Path("/cadastro-usuario")
public class CadastroUsuarioResource {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("")
	public Response setCadastro(CadastroUsuario cadastro) {
		return Response
				.status(Response.Status.OK)
				.header("Access-Control-Allow-Origin", "*")
				.build();
	}
}
