package br.com.meuspratos.resource;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.maispratos.dao.UnidadeMedidaDAO;

@Path("/unidadeMedida")
@Produces(MediaType.APPLICATION_JSON)
public class UnidadeMedidaResource {
	@Inject
	private UnidadeMedidaDAO dao;
	
	@GET
	public Response getUnidadesMedida() {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.getUnidadesMedida())
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
