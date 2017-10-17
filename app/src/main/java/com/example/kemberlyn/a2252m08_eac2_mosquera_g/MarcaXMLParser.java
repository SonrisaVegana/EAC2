package com.example.kemberlyn.a2252m08_eac2_mosquera_g;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kemberlyn on 16/10/17.
 */

public class MarcaXMLParser {

    // No fem servir namespaces
    private static final String ns = null;

    //Aquesta classe representa una entrada de noticia del RSS Feed
    public static class Entrada {
        public final String titol;        //Títol de la notcia
        public final String enllac;        //Enllaç a la notcia completa
        public final String autor;          //autor de la noticia
        public final String descripcio;     //descripcio de la noticia
        public final String datapub;        //Data de publicació
        public final String categoria;      //Categoria de la noticia
        public final String thumbnail;        //Enllaç al thumbnail de la imatge

        public Entrada(String titol, String enllac, String autor, String descripcio, String datapub, String categoria, String thumbnail) {
            this.titol = titol;
            this.enllac = enllac;
            this.autor = autor;
            this.descripcio = descripcio;
            this.datapub = datapub;
            this.categoria = categoria;
            this.thumbnail = thumbnail;
        }

        public String getTitol() {
            return titol;
        }

        public String getEnllac() {
            return enllac;
        }

        public String getAutor() {
            return autor;
        }

        public String getDescripcio() {
            return descripcio;
        }

        public String getDatapub() {
            return datapub;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getThumbnail() {
            return thumbnail;
        }

    }

    /**
     * Analiza el xml y retorma un listado de las entradas del rss
     * @param in valor retornado por obreConnexioHTTP
     * @return entrada
     * @throws XmlPullParserException
     * @throws IOException
     */

    public List<Entrada> analitza(InputStream in) throws XmlPullParserException, IOException {

        List<Entrada> entrada = null;
        try {
            //Obtenim analitzador
            XmlPullParser parser = Xml.newPullParser();

            //No fem servir namespaces
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            //Especifica l'entrada de l'analitzador
            parser.setInput(in, null);

            //Obtenim la primera etiqueta
            parser.nextTag();

            //Retornem la llista de noticies
            entrada = llegirNoticies(parser);
            return entrada;
        } finally {
            in.close();
        }
    }


    //Llegeix una llista de noticies a partir del parser i retorna una llista d'Entrades
    private List<Entrada> llegirNoticies(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entrada> llistaEntrades = new ArrayList<Entrada>();

        parser.nextTag();
        //Comprova si l'event actual és del tipus esperat (START_TAG) i del nom "rss"
        parser.require(XmlPullParser.START_TAG, ns, "channel");


        //Mentre que no arribem al final d'etiqueta
        while (parser.next() != XmlPullParser.END_TAG) {

            //Ignorem tots els events que no siguin començament d'etiqueta
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                //Saltem al seguent event
                continue;
            }

            //Obtenim el nom de l'etiqueta
            String name = parser.getName();
            Log.e("nombre etiqueta",name);

            // Si aquesta etiqueta és una entrada de noticia
            if (name.equals("item")) {
                //Afegim l'entrada a la llista
                //Log.e("nombre 2","item");
                llistaEntrades.add(llegirEntrada(parser));
            } else {
                //Si és una altra cosa la saltem
                saltar(parser);
                //Log.e("saltar","ha saltao");
            }
        }
        return llistaEntrades;
    }

    //Analitza el contingut d'una entrada. Si troba un titol, resum o enllaç, crida els mètodes de lectura
    //propis per processar-los. Si no, ignora l'etiqueta.
    private Entrada llegirEntrada(XmlPullParser parser) throws XmlPullParserException, IOException {

        //Variables per guardar els camps
        String titol = null;
        String enllac = null;
        String autor = null;
        String descripcio = null;
        String datapub = null;
        String categoria = null;
        String thumbnail = null;

        //L'etiqueta actual ha de ser "item"
        parser.require(XmlPullParser.START_TAG, ns, "item");

        //Mentre que no acabe l'etiqueta de "item"
        while (parser.next() != XmlPullParser.END_TAG) {
            //Ignora fins que no trobem un començament d'etiqueta
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            //Obtenim el nom de l'etiqueta
            String etiqueta = parser.getName();

            //Si és un títol de noticia
            if (etiqueta.equals("title")) {
                titol = llegirTitol(parser);
                //Log.d("Titulo",titol);
            }
            //Si l'etiqueta és un enllaç de notcia
            else if (etiqueta.equals("link")) {
                enllac = llegirEnllac(parser);
            }
            //Si és el autor de la noticia
            else if (etiqueta.equals("dc:creator")) {
                autor = llegirAutor(parser);
            }
            //Si l'etiqueta és la descripcio
            else if (etiqueta.equals("description")){
                descripcio = llegirDescripcio(parser);
            }
            //Si és la data de publicació
            else if (etiqueta.equals("pubDate")){
                datapub = llegirData(parser);
            }
            //Si és la categoria
            else if (etiqueta.equals("category")){
                categoria = llegirCategoria(parser);
            }
            //Si l'etiqueta és l'imatge del thumbnail
            else if (etiqueta.equals("media:thumbnail")){
                thumbnail = llegirThumbnail(parser);
            }

            else{
                //les altres etiquetes les saltem
                saltar(parser);
            }
        }

        //Creem una nova entrada amb aquestes dades i la retornem
        return new Entrada(titol, enllac, autor,descripcio,datapub,categoria,thumbnail);
    }

    /**
     * Aquesta funció serveix per saltar-se una etiqueta i les seves subetiquetes aniuades.
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */

    private void saltar(XmlPullParser parser) throws XmlPullParserException, IOException {
        //Si no és un començament d'etiqueta: ERROR
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;

        //Comprova que ha passat per tantes etiquetes de començament com acabament d'etiqueta

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    //Cada vegada que es tanca una etiqueta resta 1
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    //Cada vegada que s'obre una etiqueta augmenta 1
                    depth++;
                    break;
            }
        }
    }

    /**
     * Los siguientes metodos leen el contenido de la etiqueta correspondiente y lo retorno como un String
     * @param parser
     * @return titol,enllac, autor, descripcio, datapub, categoria, thumbnail.
     * @throws IOException
     * @throws XmlPullParserException
     */

    //Llegeix el títol
    private String llegirTitol(XmlPullParser parser) throws IOException, XmlPullParserException {
        //L'etiqueta actual ha de ser "title"
        parser.require(XmlPullParser.START_TAG, ns, "title");

        //Ontenir el text de l'etiqueta
        String titol = llegeixText(parser);

        //Fi d'etiqueta
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return titol;
    }

    //Llegeix el enllaç de la noticia
    private String llegirEnllac(XmlPullParser parser) throws IOException, XmlPullParserException {

        //L'etiqueta actual ha de ser "link"
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String enllac = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns,"link");
        return enllac;
    }

    private String llegirAutor(XmlPullParser parser) throws  IOException, XmlPullParserException{

        //L'etiqueta actual ha de ser "dc:creator"
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
        String autor = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cd:creator");
        return autor;
    }

    //Llegeix la descripcio de una notícia del rss i el retorna com String
    private String llegirDescripcio(XmlPullParser parser) throws IOException, XmlPullParserException {
        //L'etiqueta actual ha de ser "description"
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String descripcio = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return descripcio;
    }

    //Llegeix el data de publicacio de la noticia
    private String llegirData(XmlPullParser parser) throws IOException, XmlPullParserException{

        //L'etiqueta actual ha de ser "pubdate"
        parser.require(XmlPullParser.START_TAG, ns, "pubdate");
        String datapub = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns , "putdate");
        return datapub;
    }

    private String llegirCategoria(XmlPullParser parser) throws IOException, XmlPullParserException{

        //L'etiqueta actual ha de ser "category"
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String categoria = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return categoria;
    }

    private String llegirThumbnail(XmlPullParser parser) throws IOException, XmlPullParserException{

        //L'etiqueta actual ha de ser "media:thumbnail"
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        String thumb = llegeixText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "media:thumbnail");
        return thumb;
    }


    /**
     * Extrau el valor de text corresponent a l'etiqueta
     * @param parser
     * @return resultat
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String llegeixText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultat = "";

        if (parser.next() == XmlPullParser.TEXT) {
            resultat = parser.getText();
            parser.nextTag();
        }
        return resultat;
    }


}
