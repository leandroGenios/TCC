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
import br.com.maispratos.dao.PratoDAO;
import br.com.meuspratos.model.Usuario;

@Path("/prato")
@Produces(MediaType.APPLICATION_JSON)
public class PratoResource {
	@Inject
	private PratoDAO dao;
	
	@POST
	public Response setPrato(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setPrato(usuario))
					.build();				
		} catch (Exception e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@PUT
	public Response updatePrato(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.updatePrato(usuario))
					.build();				
		} catch (Exception e) {
			e.printStackTrace();
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(e.getMessage())
					.build();
		}
	}
	
	@GET
	@Path("/{idUsuario}")
	public Response listPratos(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listPratos(new IngredienteDAO().getIngredientes(idUsuario), idUsuario, ""))
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
	@Path("/meus/{idUsuario}")
	public Response listMeusPratos(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listMeusPratos(new IngredienteDAO().getIngredientes(idUsuario), idUsuario))
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
	@Path("/favorito/{idUsuario}")
	public Response listMeusFavoritos(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listMeusFavoritos(new IngredienteDAO().getIngredientes(idUsuario), idUsuario))
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
	@Path("/historico/{idUsuario}")
	public Response listPratosPreparados(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listPratosPreparados(new IngredienteDAO().getIngredientes(idUsuario), idUsuario))
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
	@Path("/avaliacao")
	public Response setAvaliacaoPrato(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setAvaliacaoPrato(usuario))
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
	@Path("/setInicioPreparo")
	public Response setPreparo(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setPreparo(usuario))
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
	@Path("/comentario")
	public Response setComentario(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.setComentario(usuario))
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
	@Path("/comentario/{idPrato}")
	public Response listComentarios(@PathParam("idPrato") int idPrato) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listComentarios(idPrato))
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
	@Path("/preparo/{idUsuario}/{idPrato}/{data}")
	public Response removerPreparo(@PathParam("idUsuario") int idUsuario, @PathParam("idPrato") int idPrato, @PathParam("data") long data) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.removerPreparo(idUsuario, idPrato, data))
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
	@Path("/favorito")
	public Response favorito(Usuario usuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(usuario.getPrato().getFavorito() == null ? dao.setFavorito(usuario) : dao.updateFavorito(usuario))
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
	@Path("/minhasAvaliacoes/{idUsuario}")
	public Response minhasAvaliacoes(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.countPratosAvaliados(idUsuario))
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
	@Path("/minhasAvaliacoesBoas/{idUsuario}")
	public Response minhasAvaliacoesBoas(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.listPratosBoaAvaliacao(idUsuario).size())
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
	@Path("/meusPratos/{idUsuario}")
	public Response meusPratos(@PathParam("idUsuario") int idUsuario) {
		try {
			return Response
					.status(Response.Status.OK)
					.entity(dao.countMeusPratos(idUsuario))
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
