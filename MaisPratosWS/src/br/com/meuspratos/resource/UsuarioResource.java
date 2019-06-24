package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.UsuarioDAO;
import br.com.meuspratos.model.Usuario;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {
	@Inject
	private UsuarioDAO dao;
	
	@POST
	public Response setUsuario(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setUsuario(usuario))
					.build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@GET
	@Path("emailExiste/{email}")
	public Response emailExiste(@PathParam("email")String email) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.emailExiste(email))
					.build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@GET
	@Path("login/{email}/{senha}")
	public Response login(@PathParam("email")String email, @PathParam("senha")String senha) {
		try {
			boolean usuarioExiste = dao.usuarioExiste(email, senha);
			if(usuarioExiste){
				return Response
						.status(Response.Status.OK)
						.entity(dao.getUsuario(email))
						.build();				
			}else{
				return Response
						.status(Response.Status.OK)
						.entity(false)
						.build();								
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
}
