package br.com.meuspratos.resource;

import java.sql.SQLException;

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
public class UsuarioResource {
	private UsuarioDAO dao;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("")
	public Response setUsuario(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getUsuarioDao().setUsuario(usuario))
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("emailExiste/{email}")
	public Response emailExiste(@PathParam("email")String email) {
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("login/{email}/{senha}")
	public Response login(@PathParam("email")String email, @PathParam("senha")String senha) {
		try {
			boolean usuarioExiste = getUsuarioDao().usuarioExiste(email, senha);
			System.out.println(usuarioExiste);
			if(usuarioExiste){
				return Response
						.status(Response.Status.OK)
						.entity(getUsuarioDao().getUsuario(email))
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
	
	private UsuarioDAO getUsuarioDao(){
		if(dao == null){
			dao = new UsuarioDAO();
		}
		return dao;
	}
}
