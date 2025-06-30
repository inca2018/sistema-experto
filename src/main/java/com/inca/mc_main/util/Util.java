package com.inca.mc_main.util;

public class Util {

    public static String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim().toLowerCase(); // normaliza para búsqueda
    }
}
