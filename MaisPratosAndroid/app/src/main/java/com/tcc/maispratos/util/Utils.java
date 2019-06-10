package com.tcc.maispratos.util;

public class Utils {

    public static String DOUBLE_TO_STRING(Double valor, int tamanho){
        return String.format("%f%n", valor).substring(0, tamanho);
    }
}
