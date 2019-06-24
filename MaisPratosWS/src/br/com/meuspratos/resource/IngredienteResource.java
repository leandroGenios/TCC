package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	@Inject
	private IngredienteDAO dao;
	
	@GET
	public Response listIngredientes() {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listIngredientes())
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
	@Path("/{idUsuario}")
	public Response getInfo(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.getIngredientes(idUsuario))
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
					.entity(dao.getIngredienteByCodigoBarras(Double.parseDouble(codBarras)))
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
	public Response setIngrediente(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setIngredienteByUsuario(usuario))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@PUT
	public Response updateIngrediente(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.updateIngredienteByUsuario(usuario))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@DELETE
	@Path("/{codUsuario}/{codIngrediente}")
	public Response deleteIngrediente(@PathParam("codUsuario") int codUsuario, @PathParam("codIngrediente") int codIngrediente) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.deleteIngredienteByUsuario(codUsuario, codIngrediente))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
}
