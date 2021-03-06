package com.tcc.maispratos.util;

import com.tcc.maispratos.R;
import com.tcc.maispratos.activity.ingrediente.IngredientesActivity;
import com.tcc.maispratos.activity.listacompras.ListaComprasActivity;
import com.tcc.maispratos.activity.perfil.AmigosActivity;
import com.tcc.maispratos.activity.perfil.PerfilActivity;
import com.tcc.maispratos.activity.prato.PratosActivity;
import com.tcc.maispratos.activity.usuario.LoginActivity;

public class MenusAction {

    public static Class<?> onActionMenu(int itemId) {
        switch (itemId) {
            case R.id.ingredientes:
                return IngredientesActivity.class;
            case R.id.pratos:
                return PratosActivity.class;
            case R.id.listaCompras:
                return ListaComprasActivity.class;
            case R.id.perfil:
                return PerfilActivity.class;
            case R.id.amigos:
                return AmigosActivity.class;
            case R.id.sair:
                return LoginActivity.class;
        }
        return null;
    }
}
