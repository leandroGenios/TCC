package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.IngredienteDAO;

@Path("/ingrediente")
@Produces(MediaType.APPLICATION_JSON)
public class IngredienteResource {
	private IngredienteDAO dao;
	
	@GET
	@Path("/{idUsuario}")
	public Response getInfo(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getIngredienteDao().getIngredientes(idUsuario))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	private IngredienteDAO getIngredienteDao(){
		if(dao == null){
			dao = new IngredienteDAO();
		}
		return dao;
	}
}
