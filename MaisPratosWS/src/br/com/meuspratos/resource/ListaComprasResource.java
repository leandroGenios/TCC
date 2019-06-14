package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.ListaComprasDAO;
import br.com.meuspratos.model.Usuario;

@Path("/listaCompras")
@Produces(MediaType.APPLICATION_JSON)
public class ListaComprasResource {
	private ListaComprasDAO dao;
	
	@GET
	@Path("/{idUsuario}")
	public Response getInfo(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getListaComprasDao().getListaCompras(idUsuario))
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
					.entity(getListaComprasDao().setIngredienteByUsuario(usuario))
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
	@Path("")
	public Response updateIngrediente(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(getListaComprasDao().updateIngredienteByUsuario(usuario))
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
					.entity(getListaComprasDao().deleteIngredienteByUsuario(codUsuario, codIngrediente))
					.build();				
		} catch (SQLException e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	private ListaComprasDAO getListaComprasDao(){
		if(dao == null){
			dao = new ListaComprasDAO();
		}
		return dao;
	}
}
