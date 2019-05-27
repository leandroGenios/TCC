package br.com.meuspratos.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.meuspratos.model.Usuario;

@Path("/usuario")
public class UsuarioResource {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("")
	public Response setCadastro(Usuario usuario) {
		/*try {
			new UsuarioDAO().setUsuario(usuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		usuario.setId(1234);
		usuario.setNome("Leandro");
		return Response
				.status(Response.Status.OK)
				.entity(usuario)
				.build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getEmail")
	public Response getEmail() {
		return Response
				.status(Response.Status.OK)
				.entity(true)
				.build();
	}
}
