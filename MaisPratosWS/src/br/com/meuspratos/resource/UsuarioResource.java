package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.UsuarioDAO;
import br.com.meuspratos.model.Usuario;

@Path("/usuario")
public class UsuarioResource {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("")
	public Response setCadastro(Usuario usuario) {
		try {
			new UsuarioDAO().setUsuario(usuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response
				.status(Response.Status.OK)
				.header("Access-Control-Allow-Origin", "*")
				.build();
	}
}
