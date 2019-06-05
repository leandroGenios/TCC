package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.IngredienteDAO;
import br.com.meuspratos.model.Usuario;

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
	
	@GET
	@Path("/getByCodigoBarras/{codBarras}")
	public Response getByCodigoBarras(@PathParam("codBarras") String codBarras) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getIngredienteDao().getIngredienteByCodigoBarras(Double.parseDouble(codBarras)))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@POST
	@Path("")
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
	
	private IngredienteDAO getIngredienteDao(){
		if(dao == null){
			dao = new IngredienteDAO();
		}
		return dao;
	}
}
