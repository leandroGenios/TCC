package br.com.meuspratos.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import br.com.maispratos.dao.IngredienteDAO;
import br.com.maispratos.dao.ListaComprasDAO;
import br.com.maispratos.dao.PratoDAO;
import br.com.maispratos.dao.UnidadeMedidaDAO;
import br.com.maispratos.dao.UsuarioDAO;

public class MyApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(IngredienteDAO.class).to(IngredienteDAO.class);
        bind(PratoDAO.class).to(PratoDAO.class);
        bind(ListaComprasDAO.class).to(ListaComprasDAO.class);
        bind(UnidadeMedidaDAO.class).to(UnidadeMedidaDAO.class);
        bind(UsuarioDAO.class).to(UsuarioDAO.class);
    }
}