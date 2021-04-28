package edu.uci.calit2.federated.client.android.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
//import androidx.appcompat.app.AppCompatActivity;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;

import static android.content.ContentValues.TAG;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */

public class MyAsyncTask extends AsyncTask<URL, Void, String> {


    private String response;
    private final WeakReference<Activity> weakActivity;
    private String mMethod;
    private File file;           //file que se manda al servidor
    String output;
    public AsyncResponse delegate = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String serverResponseMessage = "";
    String entradaExtra;
    String nombreApp;


    public MyAsyncTask(Activity activity, File file, String entradaExtra, String nombreApp, String method, AsyncResponse delegate) {
        this.weakActivity = new WeakReference<Activity>(activity);
        this.delegate = delegate;
        this.mMethod = method;
        this.file = file;
        this.entradaExtra = entradaExtra;
        this.nombreApp = nombreApp;
    }
    public interface AsyncResponse {
        void processFinish(String output);
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(URL... strings) {
        String output = null;
        switch (mMethod) {
            case "uploadWeights":
                String response = "";
                try {
                    response = uploadWeights();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (response.equals("OK")) {
                    response = "Upload Successful";
                } else {
                    response = "Upload Failed";
                }
                output = response;
                break;

            case "ismodelUpdated":
                output = isModelUpdated();
                break;
            case "getModel":
                output = downloadFiles();
                break;

                //nuevo 15-03-2021:
            case "getServidorDestino":
                output = getServidorDestino();
                break;

            case "getServidorDestinoTest":
                output = getServidorDestinoTest();
                break;

            /////////////
            }
            return output;
    }
    ///***
    //IsUpdated:
    //******



    private String isModelUpdated() {
        URL url = null;
        try {

            url = new URL("http://tftalejandroaguilera.lm.r.appspot.com");



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        try {
            if (url != null) {

                urlConnection = (HttpURLConnection) url.openConnection(); //nuevo
            }
            if (urlConnection != null) {
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection != null) {
                urlConnection.connect();
            }
            InputStream in = null;
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }
            response = readStream(in);  //RESPUESTA SI ESTA ACTUALIZADO ES "YES" , en caso contrario "NO"
            Log.i("Response", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }
    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            java.lang.String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

//****
//uploadWeights
//****
//Uploads the weights to the server.

    private String uploadWeights() throws MalformedURLException {
        URL url;

        HttpURLConnection conn; //mio


        url = new URL("YOUR URL GOES HERE");
        //PONER DISTINTA URL DEPENDIENDO DE SI GRUPO A, B o C
        if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("A")){

            url = new URL("YOUR URL GOES HERE");
        }
        if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("B")){

            url = new URL("YOUR URL GOES HERE");
        }
        if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("C")){

            url = new URL("YOUR URL GOES HERE");
        }
        if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("D")){

            url = new URL("YOUR URL GOES HERE");
        }


        try {

            conn = (HttpURLConnection) url.openConnection(); //mio
            conn.setDoOutput(true); // Allow Outputs. permite poder mandar información al servidor
            uploadWeight(conn);  //llama a uploadWeight
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponseMessage;
    }

    private void uploadWeight(HttpURLConnection conn)
    {
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE",
                    "multipart/form-data");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            conn.setRequestProperty("file", "fichero_subir2");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());   //lo que se va a mandar
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                     + "fichero_subir2" + "\"" + lineEnd); ///

            dos.writeBytes(lineEnd);
            //Write File
            dos.write(readArrayFromDevice());             //escribe llamando a readArrayFromDevice()

            dos.writeBytes(lineEnd);
           dos.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);
            int serverResponseCode = conn.getResponseCode();
            serverResponseMessage = conn.getResponseMessage();
            Log.i("Response Message: ", serverResponseMessage);
            Log.i("Response Code: ", String.valueOf(serverResponseCode));
            dos.flush();
            dos.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private byte[] readArrayFromDevice() { //se lee los datos del file
        int size = (int) file.length();  //se hace un array de longitud maxima como el fichero file
        String string = String.valueOf(size);  //pruebas
        Log.i("file.lenght= ", string);  //pruebas
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Bytes:", String.valueOf(bytes.length));
        return bytes;
    }

    //*****
    //downloadFiles
    //******
    //Downloads the global model if it is updated.

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String downloadFiles() {
        String response = "Failed";
        HttpURLConnection conn;

        try {

            URL url = new URL("YOUR URL GOES HERE");
            //DEPENDIENDO DEL GRUPO PEDIR UNA URL U OTRA:
            if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("A")){
                 url = new URL("YOUR URL GOES HERE");
            }
            if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("B")){
                 url = new URL("YOUR URL GOES HERE");
            }
            if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("C")){
                url = new URL("YOUR URL GOES HERE");
            }
            if(PrivacyLeaksReportFragment.clasificacion_usuarios.equals("D")){
                  url = new URL("YOUR URL GOES HERE");
            }


        conn = (HttpURLConnection) url.openConnection(); //mio
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            DataInputStream dis = new DataInputStream(is); //datos recibidos del servidor

            byte[] buffer = new byte[1024];
            int length;
               FileOutputStream fos = new FileOutputStream(new File(weakActivity.get().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/checkpoint4.zip")); //FUNCIONA ??
            // se hace unzip
            while ((length = dis.read(buffer)) > 0) {    //*** lo descomprime
                fos.write(buffer, 0, length);
                    unzip(weakActivity.get().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/checkpoint4.zip", weakActivity.get().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()); //FUNICONA ??
                response = "Download Succeeded";
            }
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            Log.i("Response Message: ", serverResponseMessage);
            Log.i("Response Code: ", String.valueOf(serverResponseCode));
            dis.close();
            conn.disconnect();
        } catch (MalformedURLException mue) {
            Log.e("Error", mue.getMessage());
        } catch (IOException ioe) {
            Log.e("Error", ioe.getMessage());
        } catch (SecurityException se) {
            Log.e("Error", se.getMessage());
        }
        return response;
    }

      public void unzip(String zipFile, String location) throws IOException {
            try {
                File f = new File(location);
                if (!f.isDirectory()) {
                    f.mkdirs();
                }
                ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
                try {
                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String path = location + File.separator + ze.getName();

                        if (ze.isDirectory()) {
                            File unzipFile = new File(path);
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            FileOutputStream fout = new FileOutputStream(path, false);

                            try {
                                for (int c = zin.read(); c != -1; c = zin.read()) {
                                    fout.write(c);
                                }
                                zin.closeEntry();
                            } finally {
                                fout.close();
                            }
                        }
                    }
                } finally {
                    zin.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unzip exception", e);
            }
        }
    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }

    private String getServidorDestino() {
        URL url = null;
        try {
            url = new URL("https://api-cliip-rqylggxu2a-uc.a.run.app/companies?dns="+ entradaExtra);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        HttpURLConnection urlConnection = null; //mio

        try {
            if (url != null) {

                urlConnection = (HttpURLConnection) url.openConnection();
            }
            if (urlConnection != null) {
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection != null) {
                urlConnection.connect();
            }
            InputStream in = null;
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());

            }
            response = readStream(in);  //RESPUESTA SI ESTA ACTUALIZADO ES "YES" , en caso contrario "NO"
            Log.i("Response", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        JsonParser parser = new JsonParser();

        try{
            Object obj = parser.parse(response);
            JsonObject jsonObject = (JsonObject) obj;
            //test url 15/04/2021
            String url_page = (String) jsonObject.get("name").toString();
            String[] parts_url = url_page.split(":");
            String part_url1 = parts_url[0]; // 004
            String part_url2 = parts_url[1]; // 034556


            String url_page_1 = (String) jsonObject.get("homepage_url").toString();
            String[] parts_url_1 = url_page_1.split(":");
            String part_url1_1 = parts_url_1[0]; // 004
            String part_url2_1 = parts_url_1[1]; // 034556
            String part_url3_1 = parts_url_1[2]; // 034556
            Log.i("name is", part_url1_1);
            Log.i("name is", part_url2_1);
            Log.i("name is", part_url3_1);
            String url_button = "";
            for(int i=2; i<part_url3_1.length()-2;i++){
                url_button= url_button +(part_url3_1.charAt(i));
            }
            Log.i("url_button is", url_button);
            String name = (String) jsonObject.get("name").toString();
            String cat = (String) jsonObject.get("category_list").toString();
            Log.i("name is", name);
            Log.i("cat is", cat);
            String url_final_2 = "";
            for(int i=1; i<part_url2.length()-2;i++){
                url_final_2= url_final_2 +(part_url2.charAt(i));
            }
            String url_final = "www."+part_url2+".com";
            Log.i("URL COMPAÑIA =========", "www."+part_url2+".com");
            Log.i("URL COMPAÑIA 2 =======", "www."+url_final_2+".com");


            //COGER LA RESPUESTA Y PONERLA SEGÚN LOS 5 TIPOS DE SERVIDOR DESTINO:
             String categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ADVERTISING_DST;
            String[] parts = cat.split(":");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            Log.i("cat part 2", part2);


            Log.i("cat part4", part2);
            String Mobile = "\"Mobile\"}";
            Log.i("cat mobile aux", Mobile);


            if ((part2.charAt(2) == "\"Administrative\"}".charAt(2) )&&(part2.charAt(3) == "\"Administrative\"}".charAt(3) ) ){
             categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Advertising\"}".charAt(2) )&&(part2.charAt(3) == "\"Advertising\"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ADVERTISING_DST;
            }
            if ((part2.charAt(2) == "\"Energy\"}".charAt(2) )&&(part2.charAt(3) == "\"Energy\"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Video     \"}".charAt(2) )&&(part2.charAt(3) == "\"Video     \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Travel     \"}".charAt(2) )&&(part2.charAt(3) == "\"Travel     \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Transportation    \"}".charAt(2) )&&(part2.charAt(3) == "\"Transportation    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Sustainability    \"}".charAt(2) )&&(part2.charAt(3) == "\"Sustainability    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Sports    \"}".charAt(2) )&&(part2.charAt(3) == "\"Sports    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Software    \"}".charAt(2) )&&(part2.charAt(3) == "\"Software    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Science    \"}".charAt(2) )&&(part2.charAt(3) == "\"Science    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Sales   \"}".charAt(2) )&&(part2.charAt(3) == "\"Sales   \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ADVERTISING_DST;
            }
            if ((part2.charAt(2) == "\"Real  \"}".charAt(2) )&&(part2.charAt(3) == "\"Real  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Professional \"}".charAt(2) )&&(part2.charAt(3) == "\"Professional \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Privacy \"}".charAt(2) )&&(part2.charAt(3) == "\"Privacy \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Platforms\"}".charAt(2) )&&(part2.charAt(3) == "\"Platforms\"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_SOCIAL_DST;
            }
            if ((part2.charAt(2) == "\"Events\"}".charAt(2) )&&(part2.charAt(3) == "\"Events\"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Payments           \"}".charAt(2) )&&(part2.charAt(3) == "\"Payments           \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Payments           \"}".charAt(2) )&&(part2.charAt(3) == "\"Payments           \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Navigation           \"}".charAt(2) )&&(part2.charAt(3) == "\"Navigation           \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Natural          \"}".charAt(2) )&&(part2.charAt(3) == "\"Natural          \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Music         \"}".charAt(2) )&&(part2.charAt(3) == "\"Music         \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Mobile        \"}".charAt(2) )&&(part2.charAt(3) == "\"Mobile        \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_DEVELOPMENT_DST;
            }
            if ((part2.charAt(2) == "\"Messaging        \"}".charAt(2) )&&(part2.charAt(3) == "\"Messaging        \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_SOCIAL_DST;
            }
            if ((part2.charAt(2) == "\"Media       \"}".charAt(2) )&&(part2.charAt(3) == "\"Media       \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_SOCIAL_DST;
            }
            if ((part2.charAt(2) == "\"Manufacturing      \"}".charAt(2) )&&(part2.charAt(3) == "\"Manufacturing      \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Lending      \"}".charAt(2) )&&(part2.charAt(3) == "\"Lending      \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Internet     \"}".charAt(2) )&&(part2.charAt(3) == "\"Internet     \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_SOCIAL_DST;
            }
            if ((part2.charAt(2) == "\"Information    \"}".charAt(2) )&&(part2.charAt(3) == "\"Information    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Health   \"}".charAt(2) )&&(part2.charAt(3) == "\"Health   \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Hardware  \"}".charAt(2) )&&(part2.charAt(3) == "\"Hardware  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Government  \"}".charAt(2) )&&(part2.charAt(3) == "\"Government  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Gaming \"}".charAt(2) )&&(part2.charAt(3) == "\"Gaming \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Food  \"}".charAt(2) )&&(part2.charAt(3) == "\"Food  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Food  \"}".charAt(2) )&&(part2.charAt(3) == "\"Food  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }

            if ((part2.charAt(2) == "\"Education \"}".charAt(2) )&&(part2.charAt(3) == "\"Education \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Design \"}".charAt(2) )&&(part2.charAt(3) == "\"Design \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_DEVELOPMENT_DST;
            }
            if ((part2.charAt(2) == "\"Data \"}".charAt(2) )&&(part2.charAt(3) == "\"Data \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ANALYTICS_DST;
            }
            if ((part2.charAt(2) == "\"Content       \"}".charAt(2) )&&(part2.charAt(3) == "\"Content       \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Consumer      \"}".charAt(2) )&&(part2.charAt(3) == "\"Consumer      \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ANALYTICS_DST;
            }
            if ((part2.charAt(2) == "\"Community     \"}".charAt(2) )&&(part2.charAt(3) == "\"Community     \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_SOCIAL_DST;
            }
            if ((part2.charAt(2) == "\"Commerce    \"}".charAt(2) )&&(part2.charAt(3) == "\"Commerce    \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_ADVERTISING_DST;
            }
            if ((part2.charAt(2) == "\"Clothing   \"}".charAt(2) )&&(part2.charAt(3) == "\"Clothing   \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Biotechnology  \"}".charAt(2) )&&(part2.charAt(3) == "\"Biotechnology  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Artificial  \"}".charAt(2) )&&(part2.charAt(3) == "\"Artificial  \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Apps \"}".charAt(2) )&&(part2.charAt(3) == "\"Apps \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }
            if ((part2.charAt(2) == "\"Agriculture \"}".charAt(2) )&&(part2.charAt(3) == "\"Agriculture \"}".charAt(3) ) ){
                categoria_serv_dst = PrivacyLeaksReportFragment.STRING_INTERNAL_DST;
            }

            boolean boolcat1 = (part2.charAt(2) == Mobile.charAt(2));
            Log.i("cat boll4", ""+boolcat1);


            PrivacyLeaksReportFragment.mapa_tipos_servidores.put(nombreApp, categoria_serv_dst);
            PrivacyLeaksReportFragment.mapa_url_compañias.put(nombreApp, url_button);


        } catch (Exception e) {
            e.printStackTrace();
            PrivacyLeaksReportFragment.mapa_tipos_servidores.put(nombreApp, PrivacyLeaksReportFragment.STRING_ADVERTISING_DST);
            PrivacyLeaksReportFragment.mapa_url_compañias.put(nombreApp, "www.1010data.com");
        }


        return response;
    }

    //test con url mala
    private String getServidorDestinoTest() {
        URL url = null;
        try {

            url = new URL("http://tftalejandroaguilera.lm.r.appspot.com/json");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        try {
            if (url != null) {

                urlConnection = (HttpURLConnection) url.openConnection();
            }
            if (urlConnection != null) {
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection != null) {
                urlConnection.connect();
            }
            InputStream in = null;
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());  //leemos lo que nos pasa el servidor en in

            }
            response = readStream(in);  //RESPUESTA SI ESTA ACTUALIZADO ES "YES" , en caso contrario "NO"
            Log.i("Response", response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        JsonParser parser = new JsonParser();

        try{
            Object obj = parser.parse(response);
            JsonObject jsonObject = (JsonObject) obj;
            String name = (String) jsonObject.get("name").toString();
            Log.i("name is", name);

            String age = (String) jsonObject.get("age").toString();
            Log.i("age is", age);
            //array

            JsonArray jsonArray = (JsonArray) jsonObject.get("company");
            Iterator<JsonElement> iterator = jsonArray.iterator();

            while(iterator.hasNext()) {

                PrivacyLeaksReportFragment.mapa_tipos_servidores.put(nombreApp, String.valueOf((iterator.next()).getAsJsonObject().get("category")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }
}