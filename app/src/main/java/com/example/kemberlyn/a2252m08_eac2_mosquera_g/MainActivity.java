package com.example.kemberlyn.a2252m08_eac2_mosquera_g;
import com.example.kemberlyn.a2252m08_eac2_mosquera_g.DescargarDades;

import com.example.kemberlyn.a2252m08_eac2_mosquera_g.MarcaXMLParser.Entrada;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    //declarar URL y ubicación para datos sin conexión.
    public static final String sURL = "http://estaticos.marca.com/rss/portada.xml";
    public String dirCache; //Investigar

    // Conexion por WiFi
    private static boolean connectatWifi = false;
    // Conexion por 3G
    private static boolean connectat3G = false;

    //Variables de la UI
    private RecyclerView rView;

    private ProgressBar progressBar;
//    private List<Entrada> entrades;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicializar RecyclerView componentes de la UI
        rView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);


        //Mirem si hi ha connexió de xarxa
        EstatXarxa();
        //Carreguem les noticies a un fil independent fent servir AsyncTask
        //Carreguem noticies
        carregaNoticies();

//        TascaDescarregaRSS tasca = new TascaDescarregaRSS();
//        tasca.execute(sURL);
    }

    /**
     * Verificar el tipo de conexion establecida por la red
     * WiFi o 3G
     */

    private void EstatXarxa() {
        //Obtener el gestor de conexiones de red
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Obtener el estado de la red movil
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo == null)
            connectat3G = false;
        else
            connectat3G = networkInfo.isConnected();

        //Obtener el estado de la conexion wifi
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo == null)
            connectatWifi = false;
        else
            connectatWifi = networkInfo.isConnected();

    }

    /**
     * Si la conexión esta activa lanzamos el AsyncTask
     */

    public void carregaNoticies() {

        if (connectat3G || connectatWifi) {
            //Mostrarà la barra de progrés (propietat indeterminate a true i  style="?android:attr/progressBarStyleLarge")
            progressBar.setIndeterminate(true);
            //Interpretara el XML
            new TascaDescarregaRSS().execute(sURL);
            //Guardara en la BD
            ///////////////////////////////////////////
            //            Implementar BD             //
            ///////////////////////////////////////////

        } else {
            ///////////////////////////////////////////
            //     Implementar datos en cache        //
            ///////////////////////////////////////////
            Toast.makeText(this, "No hi ha connexió", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Implementación de AsyncTask para descargar el xml de marca.com
     */

    private class TascaDescarregaRSS extends AsyncTask<String, Integer, String> {

        //Iniciar con la barra de progeso
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);

        }

        //Aqui realizamos la conexión y cargamos el xml
        @Override
        protected String doInBackground(String... url) {
            //Instancia a la clase aux para la descarga del XML
            DescargarDades descargarDades = new DescargarDades();

            try {
                return descargarDades.carregaXMLdelaXarxa(url[0]);
            } catch (IOException e) {
                return "Error de Connexió";
            } catch (XmlPullParserException e) {
                return "Error a l'analitzar l'XML";
            }

        }

        //Una vez descargada la info del XML la mostraremos en el RecyclerView
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("OK")){
                Log.d("Titulo", )
                //Mostrar contenido en el RecyclerView
                rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                MyAdapter adapter = new MyAdapter(entrades,getApplicationContext());
                rView.setAdapter(adapter);
                rView.setItemAnimator(new DefaultItemAnimator());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

    }

//    /**
//     * Descarrega XML de marca.com
//     *
//     * @param urlString
//     * @return htmlString
//     * @throws XmlPullParserException
//     * @throws IOException
//     */
//    private String carregaXMLdelaXarxa(String urlString) throws XmlPullParserException, IOException {
//        InputStream stream = null;
//        //Creem una instancia de l'analitzador
//        MarcaXMLParser analitzador = new MarcaXMLParser();
//
//        //Llista de entrades de noticies
//        entrades = new ArrayList<Entrada>();
//
//        //Cadena on construirem el codi HTML que mostrara el widget webView
//        StringBuilder htmlString = new StringBuilder();
//
//
//        try {
//            //Obrim la connexio);
//            stream = ObreConnexioHTTP(urlString);
//
//            //Obtenim la llista d'entrades a partir de l'stream de dades
//            entrades = analitzador.analitza(stream);
//        } catch (Exception e) {
//            //Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//
//            e.printStackTrace();
//        } finally {
//            //Tanquem l'stream una vegada hem terminat de treballar amb ell
//            if (stream != null) {
//                stream.close();
//            }
//        }
//
//        //analitzador.parse() retorna una llista (entrades) d'entrades de noticies (objectes
//        //de la classe Entrada. Cada objecte representa un post de l'XML Feed. Ara es processen
//        //les entrades de la llista per crear un codi HTML. Per cada entrada es crea un enllaç
//        //a la noticia completa
//
//        //Si tenim noticies
//        if (entrades != null) {
//            return "Ok";
//
//        }else{
//            return "NO";
//        }
//
//    }
//
//
//    //Metodos auxiliares para descargar RSS
//    private BufferedInputStream ObreConnexioHTTP(String adrecaURL) throws IOException {
//        BufferedInputStream in = null;
//        int resposta;
//
//        URL url = new URL(adrecaURL);
//        URLConnection connexio = url.openConnection();
//
//        if (!(connexio instanceof HttpURLConnection))
//            throw new IOException("No connexió HTTP");
//
//        try {
//            HttpURLConnection httpConn = (HttpURLConnection) connexio;
//            httpConn.setAllowUserInteraction(false);
//            httpConn.setInstanceFollowRedirects(true);
//            httpConn.setRequestMethod("GET");
//            httpConn.connect();
//
//            resposta = httpConn.getResponseCode();
//            if (resposta == HttpURLConnection.HTTP_OK) {
//                in = new BufferedInputStream(httpConn.getInputStream());
//            }
//        } catch (Exception ex) {
//            throw new IOException("Error connectant");
//        }
//
//        return in;
//    }

}

