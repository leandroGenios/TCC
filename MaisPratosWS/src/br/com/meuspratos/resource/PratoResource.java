package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.PratoDAO;
import br.com.meuspratos.model.Usuario;

@Path("/prato")
@Produces(MediaType.APPLICATION_JSON)
public class PratoResource {
	private PratoDAO dao = new PratoDAO();
	
	@POST
	@Path("/ingrediente")
	public Response setIngrediente(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getIngredienteDao().setIngredienteByUsuario(usuario))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	private PratoDAO getIngredienteDao(){
		if(dao == null){
			dao = new PratoDAO();
		}
		return dao;
	}
}
