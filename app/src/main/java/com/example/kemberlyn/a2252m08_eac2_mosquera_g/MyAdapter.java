package com.example.kemberlyn.a2252m08_eac2_mosquera_g;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kemberlyn on 08/10/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ElMeuViewHolder> {
    //variables
    private ArrayList<ItemFila> items;
    private Context context;

    //Constructor
    public MyAdapter(ArrayList<ItemFila> items, Context context){
        this.items = items;
        this.context = context;
    }

    /**
     * Crear nuevas filas. Referenciar el layout fila.xml
     * @param parent
     * @param viewType
     * @return viewHolder
     */
    @Override
    public ElMeuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Crear nueva vista
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila, null);
        //Crear ViewHolder
        ElMeuViewHolder viewHolder = new ElMeuViewHolder(itemLayoutView);
        return viewHolder;
    }

    /**
     *
     * @return la cantidad de los datos
     */

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Cargar los widget con los datos
     * @param holder
     * @param position posicion actual del elemento de la lista index para recorrer la lista
     */
    @Override
    public void onBindViewHolder(ElMeuViewHolder holder, int position) {
        holder.texto.setText(items.get(position).getTexto());
        holder.imagen.setImageResource(items.get(position).getId_imagen());

    }

    //Definim el nostre ViewHolder, és a dir, un element de la llista en qüestió
    public static class ElMeuViewHolder extends RecyclerView.ViewHolder{
        //Contingut del layout
        protected ImageView imagen;
        protected TextView texto;

        public ElMeuViewHolder(View itemView) {
            super(itemView);
            //Referenciem els elements del layout
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            texto = (TextView) itemView.findViewById(R.id.texto);
        }
    }
}
