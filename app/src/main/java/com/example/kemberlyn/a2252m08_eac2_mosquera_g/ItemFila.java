package com.example.kemberlyn.a2252m08_eac2_mosquera_g;

/**
 * Esta clase representa el contenido de una fila de la lista del RecyclerView
 * Created by Kemberlyn on 08/10/2017.
 */

public class ItemFila{
    private String texto;
    private int id_imagen;

    public ItemFila(String texto, int id_imagen) {
        this.texto = texto;
        this.id_imagen = id_imagen;
    }

    public String getTexto() {
        return texto;
    }

    public int getId_imagen() {
        return id_imagen;
    }
}
