package com.example.kemberlyn.a2252m08_eac2_mosquera_g;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    //declarar URL y ubicación para datos sin conexión.
    public static String sURL = "http://estaticos.marca.com/rss/portada.xml";
    public String dirCache; //Investigar

    //Variables de la UI
    private RecyclerView rView;
    private ArrayList<ItemFila> items;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicializar RecyclerView componentes de la UI
        rView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);

        //Cargar dades demo
        items = new ArrayList<ItemFila>();
        for(int i= 0; i<20; i++){
            items.add(new ItemFila("Texto para el titutal demo" +
                    "numero "+i, R.drawable.imagen));
        }
        rView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(items,this);
        rView.setAdapter(adapter);
        rView.setItemAnimator(new DefaultItemAnimator());

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TascaDescargaRSS extends AsyncTask<String, Integer, Void> {

        //Iniciar con la barra de progeso
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);

        }

        //Aqui realizamos la conexión
        @Override
        protected Void doInBackground(String... url) {
            try {
                //URL  a procesar
                URL xmlRSS = new URL(url[0]);
                //Hacer la conexión
                HttpsURLConnection connection = (HttpsURLConnection) xmlRSS.openConnection();
                //input y buffer para leer la información
                byte[] bufferXML = new byte[1024];
            }catch(IOException exception){
                Log.d("ERR","No ni ha conexió!");
                return null;
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }

}
