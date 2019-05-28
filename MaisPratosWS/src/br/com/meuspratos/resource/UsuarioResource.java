package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.UsuarioDAO;
import br.com.meuspratos.model.Usuario;

@Path("/usuario")
public class UsuarioResource {
	private UsuarioDAO dao;
	
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
	public Response getEmail(String email) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getUsuarioDao().emailExiste(email))
					.build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	private UsuarioDAO getUsuarioDao(){
		if(dao == null){
			dao = new UsuarioDAO();
		}
		return dao;
	}
}
