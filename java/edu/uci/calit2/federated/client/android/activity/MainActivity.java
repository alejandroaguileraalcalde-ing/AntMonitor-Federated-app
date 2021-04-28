/*
Alejandro Aguilera Alcalde
 */

package edu.uci.calit2.federated.client.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

//import androidx.appcompat.app.AppCompatActivity;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.common.util.ArrayUtils;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import edu.uci.calit2.federated.R;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */



public class MainActivity extends AppCompatActivity {
    byte[] graphDef;
    byte[] variableAuxCheck;
    public static  Session sess;
    Graph graph;
    File file1;
    File file_leer;
    File file;   //este file es el que se manda al servidor ~~~~
    File fileA;   //este file es el que se manda al servidor ~~~~
    File fileB;   //este file es el que se manda al servidor ~~~~
    File fileC;   //este file es el que se manda al servidor ~~~~
    File file_descargado;
    File textFile;
    ArrayList<String> pesos = new ArrayList<>();
    Tensor<String> checkpointPrefix;
    String checkpointDir;
    InputStream inputCheck;
    float num_epoch = 0;
    public static boolean isModelUpdated = false;
    int modelctr = 11000;
    ArrayList<Float> y_mejoras_w = new ArrayList<Float>();
    ArrayList<String> x_mejoras_w = new  ArrayList<String>();
    ArrayList<Float> y_mejoras_b = new  ArrayList<Float>();
    ArrayList<String> x_mejoras_b = new ArrayList<String>();

    //listas para pesos Ant
    ArrayList<Float> y_mejoras_location = new ArrayList<Float>();
    ArrayList<String> x_mejoras_location = new  ArrayList<String>();

    ArrayList<Float> y_mejoras_email = new  ArrayList<Float>();
    ArrayList<String> x_mejoras_email = new ArrayList<String>();

    ArrayList<Float> y_mejoras_imei = new ArrayList<Float>();
    ArrayList<String> x_mejoras_imei = new  ArrayList<String>();

    ArrayList<Float> y_mejoras_device = new  ArrayList<Float>();
    ArrayList<String> x_mejoras_device = new ArrayList<String>();

    public static int num= 0; //contador para el array de las graficas
    boolean false_var = false;



    TextView B;
    TextView W;
    TextView Wtest;
    TextView Btest;
    TextView Y;
    public static TextView titulo;
    public static   TextView subtitulo;
    Button button ;
    EditText epochs;

    //Ant --> epoca = 1
    EditText location;
    EditText email;
    EditText device;
    EditText imei;

    public static   Button mostrar_resumen;
    public static  TextView resumen_app;
    public static   TextView explicacion;
    public static   Button bien;
    public static   Button mal;
    public static   Button mostrar_resumen_final;

    public static   float umbral = 10;
    public static float umbral_verde = 3;
    public static float umbral_naranja = 10;
    public static float umbral_rojo = 15;

    // pesos a cambiar:
    public static  float Plocation = 9;
    public static float Pemail = 8;
    public static  float Pimei = 3;
    public static  float Pdevice = 2;
    public static  float Pserialnumber = 1;
    public static  float Pmacaddress = 3;
    public static  float Padvertiser = 4;

    boolean actualizacion_datos_activity = false;


    //ventanas


    Button info;
    public static  int nivel_color = 0;
    public static String clasificacion_usuarios = "B"; //A (menos estrictos con los pesos), B(un nivel más normal), C( nivel muy alto)

    //pasar variables a otra actividad:
    public static final String PLOCATION = "Plocation";
    public static final String PEMAIL = "Pemail";
    public static final String PDEVICE = "Pdevice";
    public static final String PIMEI = "Pimei";
    public static final String PSERIALNUMBER = "Pserialnumber";
    public static final String PMACADDRESS = "Pmacaddresss";
    public static final String PADVERTISER = "Padvertiser";

    public static final String ACTUALIZACION_DATOS_ACTIVITY = "actualizacion_datos_activity";

    public static final String AUX_BUTTON = "aux_button";
    public static final String CLICK_BIEN = "click_bien";
    public static final String CLICK_MAL = "click_mal";

    public  boolean aux_button = false;
    public boolean click_bien = false;
    public boolean click_mal = false;
    public static boolean click_mal2 = false;
    public static boolean click_bien2 = false;
    public static boolean aux_button2 = false;
    public static int posicion_button = 0;

    //lista de apps analizadas:

    ArrayList<ArrayList<Float>> lista_apps_analizadas = new ArrayList<ArrayList<Float>>();

    ArrayList<Float> app1 = new  ArrayList<Float>();
    ArrayList<Float> app2 = new  ArrayList<Float>();
    ArrayList<Float> app3 = new  ArrayList<Float>();
    ArrayList<Float> app4 = new  ArrayList<Float>();

    ArrayList<Entidad> listItems = new ArrayList<>();



   //lista de apps:
   private ListView lvItems;
    private Adaptador adaptador;




    //???



    static {
        System.loadLibrary("tensorflow_inference");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //igual quitar
       file1 = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba"); // HAY QUE PONER UN FICHERO AQUI PARA PROBAR SI SE MANDA BIEN AL SERVIDOR
      // file =  new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/Weights.bin"); // HAY QUE PONER UN FICHERO AQUI PARA PROBAR SI SE MANDA BIEN AL SERVIDOR
        setContentView(R.layout.activity_main);
        InputStream inputStream;
        //EN UN EJEMPLO REAL SE LLAMARIA A LA SIGUIENTE FUNCION:
        // initializeGraph();
        try {
         //Place the .pb file generated before in the assets folder and import it as a byte[] array
         //  inputStream = getAssets().open("graph.pb");   //PROBAR OTRO GRAPH
            //voy a cambiarlo por ANT
          // inputStream = getAssets().open("graph5.pb");   //PROBAR OTRO GRAPH
            inputStream = getAssets().open("graph_Ant_v13.pb");   //PROBAR OTRO GRAPH
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            graphDef = output.toByteArray();  // ARRAY CON EL GRAPH
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create a variable of class org.tensorflow.Graph:
        Graph graph = new Graph();
        sess = new Session(graph);
        //load the graph from the graphdef
        graph.importGraphDef(graphDef);
//        inferenceInterface = new TensorFlowInferenceInterface;
//        inferenceInterface.initializeTensorFlow(getAssets(), MODEL_FILE);
        sess.runner().addTarget("init").run(); //INICIALIZAR EL GRAPH
       Toast.makeText(getApplicationContext(), "Initialized" + sess.toString(), Toast.LENGTH_SHORT).show();

        //VISTAS Y BOTONES
 /*
        W = (TextView) findViewById(R.id.W);
        B = (TextView) findViewById(R.id.B);
        Wtest = (TextView) findViewById(R.id.Wtest);
         Btest = (TextView) findViewById(R.id.Btest);
         button = (Button) findViewById(R.id.button);
         epochs = (EditText) findViewById(R.id.epochs);
        Y = (TextView) findViewById(R.id.Y);

        //mi boton de pruebas
        final Button buttonPruebas = (Button) findViewById(R.id.buttonPrueba);
*/
        //graficas
       /* lineChartView  = findViewById(R.id.chart);
        lineChartView2  = findViewById(R.id.chart2);
        lineChartView3  = findViewById(R.id.chart3);
        lineChartView4  = findViewById(R.id.chart4);

        */

        W = (TextView) findViewById(R.id.Text);
        Y = (TextView) findViewById(R.id.Y);
        titulo = (TextView) findViewById(R.id.titulo);
        subtitulo = (TextView) findViewById(R.id.subtitulo);
        // Ant
     /*   location = (EditText) findViewById(R.id.location);
        email = (EditText) findViewById(R.id.email);
        device = (EditText) findViewById(R.id.device);
        imei = (EditText) findViewById(R.id.imei);

      */

        //mostrar_resumen = (Button) findViewById(R.id.mostrar_resumen);
        bien = (Button) findViewById(R.id.bien);
        mal = (Button) findViewById(R.id.mal);
        info = (Button) findViewById(R.id.info);
        mostrar_resumen_final = (Button) findViewById(R.id.mostrar_resumen_final);


        resumen_app = (TextView) findViewById(R.id.resumen_app);
      //  explicacion = (TextView) findViewById(R.id.explicacion);

        //mock data de apps analizadas:
        lvItems = (ListView) findViewById(R.id.lvItems);
        adaptador = new Adaptador(this, GetArrayItems());
        lvItems.setAdapter(adaptador);




        //datos que se han cambiado desde otra activity:
        Intent intent = getIntent();
        actualizacion_datos_activity = intent.getBooleanExtra(MainActivity.ACTUALIZACION_DATOS_ACTIVITY, false);
        if ( actualizacion_datos_activity ) {
            Plocation = intent.getFloatExtra(MainActivity.PLOCATION, 0);
            Pemail = intent.getFloatExtra(MainActivity.PEMAIL, 0);
            Pdevice = intent.getFloatExtra(MainActivity.PDEVICE, 0);
            Pimei = intent.getFloatExtra(MainActivity.PIMEI, 0);
            Padvertiser = intent.getFloatExtra(MainActivity.PADVERTISER, 0);
            Pmacaddress = intent.getFloatExtra(MainActivity.PMACADDRESS, 0);
            Pserialnumber = intent.getFloatExtra(MainActivity.PSERIALNUMBER, 0);

            //agrupo al usuario en un grupo dependiendo de los pesos de los datos filtrados:
            float sumapesos = Plocation + Pemail + Pdevice + Pimei + Padvertiser + Pmacaddress + Pserialnumber;
            if(sumapesos < 20){
                clasificacion_usuarios = "A"; //Poco estricto
            }
            if((sumapesos >= 20)||(sumapesos < 32)){
                clasificacion_usuarios = "B"; //Normal
            }
            if(sumapesos >= 32){
                clasificacion_usuarios = "C"; //Muy estricto
            }

            Toast.makeText(getApplicationContext(), "Pesos actualizados correctamente", Toast.LENGTH_SHORT).show();

        }
       // Toast.makeText(getApplicationContext(), "Plocation: " + Plocation, Toast.LENGTH_SHORT).show();
/*
        Intent intent2 = getIntent();
        aux_button = intent2.getBooleanExtra(MainActivity.AUX_BUTTON, false);
        click_mal = intent2.getBooleanExtra(MainActivity.CLICK_MAL, false);
        click_bien = intent2.getBooleanExtra(MainActivity.CLICK_BIEN, false);

 */


        //para funcionar bien y mal desde la lista:






        //BOTON UPLOAD
        final Button upload = findViewById(R.id.uploadWeights);
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                writeFileAnt();
              MyAsyncTask uploadWeights = new MyAsyncTask(MainActivity.this, file, "","","uploadWeights", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        Log.i("Output: uploadWeights", result);
                        Toast.makeText(MainActivity.this, "7 Weights Successfully Uploaded", Toast.LENGTH_SHORT).show();



                    }
                });
                uploadWeights.execute();

                Toast.makeText(MainActivity.this, "7 Weights updated", Toast.LENGTH_SHORT).show();


                /*//extra
                writeFileAnt();
                readFileAnt();

                 */
            }


              /*
                float n_epochs = Float.parseFloat(epochs.getText().toString());
                float num1 = (float) Math.random();
                try (org.tensorflow.Tensor input = Tensor.create(num1);
                     Tensor target = Tensor.create(2*num1 + 3)) {
                    for(int epoch = 0; epoch <= n_epochs; epoch++) {
                        sess.runner().feed("input", input).feed("target", target).addTarget("train").run();
                        ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
                        W.setText((Float.toString(values.get(0).floatValue())));
                        B.setText(Float.toString(values.get(1).floatValue()));
                    }
                }
                getWeightsPrueba();
            }

               */
        });




        final Button getModel = findViewById(R.id.getModel);
        getModel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(MainActivity.this, file,"","", "ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        //If True, get Global Model
                        MyAsyncTask getGlobalModel = new MyAsyncTask(MainActivity.this, file, "","","getModel", new MyAsyncTask.AsyncResponse() {
                            @Override
                            public void processFinish(String result) {
                                Log.i("Output: GetGlobalModel", result);
                            }
                        });
                        getGlobalModel.execute();
                        Toast.makeText(MainActivity.this, "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                        Log.i("Output: isModelUpdated", "Done");

                    }
                });
                isGlobalModelUpdated.execute();
                file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "graph_pesos");
                // file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "checkpoint_name_1001-.ckpt.meta"); //poner el fichero checkpoints_name.cktp.meta
                //     file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "+checkpoints_name_1001-.ckpt.meta"); //poner el fichero checkpoints_name.cktp.meta
                //esto es para la prueba, guardo en la variable file el fichero descargado del servidor para poder mandarlo y ver si funciona.
                //    file = file_descargado;
                Toast.makeText(MainActivity.this, "se ha guardado el nuevo checkpoint", Toast.LENGTH_SHORT).show();
                //actualizo el etado del modelo.
                isModelUpdated = true;
            }
        });
      /*    // este esta BIEN:
        //BOTON GETMODEL ***VERSION NORMAL***

        Button getModel = findViewById(R.id.getModel);
        getModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(MainActivity.this, file, "ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        //If True, get Global Model
                            MyAsyncTask getGlobalModel = new MyAsyncTask(MainActivity.this, file, "getModel", new MyAsyncTask.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    Log.i("Output: GetGlobalModel", result);
                                }
                            });
                            getGlobalModel.execute();
                            Toast.makeText(MainActivity.this, "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                        Log.i("Output: isModelUpdated", "Done");

                    }
                });
                isGlobalModelUpdated.execute();
                file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "graph_pesos");
                // file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "checkpoint_name_1001-.ckpt.meta"); //poner el fichero checkpoints_name.cktp.meta
           //     file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "+checkpoints_name_1001-.ckpt.meta"); //poner el fichero checkpoints_name.cktp.meta
                  //esto es para la prueba, guardo en la variable file el fichero descargado del servidor para poder mandarlo y ver si funciona.
              //    file = file_descargado;
                  Toast.makeText(MainActivity.this, "se ha guardado el nuevo checkpoint", Toast.LENGTH_SHORT).show();
                  //actualizo el etado del modelo.
                  isModelUpdated = true;
            }
        });
            */



    /*

        //BOTON PRUEBA Hasta guardar fichero weights.
        buttonPruebas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float n_epochs = Float.parseFloat(epochs.getText().toString());
                float num1 = (float) Math.random();
                num_epoch = n_epochs;
                int epochs = (int)n_epochs;
               // initializeGraphPrueba();
          if (isModelUpdated) {

                    initializeGraph_check();  //para restaurar el graph con los checkpoints actualizados.
                    isModelUpdated = false;
                }

                predictPrueba(num1);  //hay que cambiarlo para que sea igaul pero saque el output predecido usando el modelo. Y se llamaria despues de train
                num++;
                trainPrueba(num1, epochs);
                Graficar_w();
                Graficar_b();

               // Funcionar_salida(1);   //con la entrada aleatoria da una salida usando el modelo de machine learning entrenado. Pongo de entrada "1" para ver que funciona de una forma mas rapida
              //  finalSave1(); //  #No funciona

             //      finalSave_linear();
            //    file_descargado = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "checkpoint.zip"); //poner el fichero checkpoints_name.cktp.meta
                //esto es para la prueba, guardo en la variable file el fichero descargado del servidor para poder mandarlo y ver si funciona.
            //    file = file_descargado;
                //predict function -->hacer la mia para mi modelo
                // train()

               // getWeights();



            }
        });
     */
/*
        //BOTON MOSTRAR RESUMEN
        mostrar_resumen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LO COMENTO 12/02/2021
                //    Funcionar_salida(1);
                //    Funcionar_salida_Ant(1, 1,0,0);
                //   num_epoch = 1;

                if (isModelUpdated) {

                    initializeGraph_check();  //para restaurar el graph con los checkpoints actualizados.
                    isModelUpdated = false;
                }
                num_epoch = 1;
                float n_epochs = 1;
                float location1 = Float.parseFloat(location.getText().toString());
                float email1 = Float.parseFloat(email.getText().toString());
                float device1 = Float.parseFloat(device.getText().toString());
                float imei1 = Float.parseFloat(imei.getText().toString());

              //  Funcionar_salida_Ant(location1, email1,imei1,device1);
                Funcionar_salida_Ant_umbral(location1, email1,imei1,device1);
                //prueba
                titulo.setVisibility(View.VISIBLE);
                resumen_app.setVisibility(View.VISIBLE);
                bien.setVisibility(View.VISIBLE);

                subtitulo.setVisibility(View.VISIBLE);

                //extra para pruebas:

            }
        });

 */
      /*  //BOTON MOSTRAR RESUMEN
        mostrar_resumen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (isModelUpdated) {

                    initializeGraph_check();  //para restaurar el graph con los checkpoints actualizados.
                    isModelUpdated = false;
                }
                num_epoch = 1;

              //  lvItems.setVisibility(View.GONE);

                ArrayList<Float> lista_location = new ArrayList<Float>();
                ArrayList<Float> lista_email = new ArrayList<Float>();
                ArrayList<Float> lista_device = new ArrayList<Float>();
                ArrayList<Float> lista_imei = new ArrayList<Float>();
                float n_epochs = 1;
                // int num_apps = GetArrayItemsAux().size();
                int num_apps = listItems.size();
                for (int i=0; i<num_apps; i++){

                    lista_location.add(listItems.get(i).getLocation());
                    lista_email.add(listItems.get(i).getEmail());
                    lista_device.add(listItems.get(i).getDevice());
                    lista_imei.add(listItems.get(i).getImei());




                }
                //   float location1 = Float.parseFloat(location.getText().toString());


                //  Funcionar_salida_Ant(location1, email1,imei1,device1);
                for (int i=0; i<num_apps; i++){
                    //hay que cambiar el color del borde según sea el nivel. ver como hacerlo.

                    listItems.get(i).setResumen(Funcionar_salida_Ant_umbral_lista_Dentro(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i))); //devuleve el texto de dentro
                   listItems.get(i).setValor_nivel( Funcionar_salida_Ant_umbral_lista_Nivel(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i)));  //devuelve nivel: bajo, medio, etc


                    listItems.get(i).setColor(Funcionar_salida_Ant_umbral_Color(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i))); //devuleve el color: 0, 1,2
                }


                //prueba

                //poner visible la listView y antes que este GONE:

                lvItems.setVisibility(View.VISIBLE);



            }
        });


       */
        //BOTON MOSTRAR RESUMEN FINAL
        mostrar_resumen_final.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                // LO COMENTO 12/02/2021
                //    Funcionar_salida(1);
                //    Funcionar_salida_Ant(1, 1,0,0);
                //   num_epoch = 1;

                if (isModelUpdated) {

                    initializeGraph_check();  //para restaurar el graph con los checkpoints actualizados.
                    isModelUpdated = false;
                }
                num_epoch = 1;



                ArrayList<Float> lista_location = new ArrayList<Float>();
                ArrayList<Float> lista_email = new ArrayList<Float>();
                ArrayList<Float> lista_device = new ArrayList<Float>();
                ArrayList<Float> lista_imei = new ArrayList<Float>();
                ArrayList<Float> lista_serialnumber = new ArrayList<Float>();
                ArrayList<Float> lista_macaddress = new ArrayList<Float>();
                ArrayList<Float> lista_advertiser = new ArrayList<Float>();
                float n_epochs = 1;
               // int num_apps = GetArrayItemsAux().size();
                int num_apps = listItems.size();
                for (int i=0; i<num_apps; i++){

                    lista_location.add(listItems.get(i).getLocation());
                    lista_email.add(listItems.get(i).getEmail());
                    lista_device.add(listItems.get(i).getDevice());
                    lista_imei.add(listItems.get(i).getImei());
                    lista_serialnumber.add(listItems.get(i).getSerialnumber());
                    lista_macaddress.add(listItems.get(i).getMacaddresss());
                    lista_advertiser.add(listItems.get(i).getAdvertiser());




                }
             //   float location1 = Float.parseFloat(location.getText().toString());


                //  Funcionar_salida_Ant(location1, email1,imei1,device1);
                for (int i=0; i<num_apps; i++){
                    //hay que cambiar el color del borde según sea el nivel. ver como hacerlo. 

                    listItems.get(i).setResumen(Funcionar_salida_Ant_umbral_lista_Dentro(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i))); //devuleve el texto de dentro
                     listItems.get(i).setValor_nivel( Funcionar_salida_Ant_umbral_lista_Nivel(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i))); //devuelve nivel: bajo, medio, etc
                    listItems.get(i).setColor(Funcionar_salida_Ant_umbral_Color(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i)));  //devuleve el color: 0, 1,2
                }


                //prueba

                //poner visible la listView y antes que este GONE:

                lvItems.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                getModel.setVisibility(View.VISIBLE);
                upload.setVisibility(View.VISIBLE);
                Y.setVisibility(View.VISIBLE);

            }
        });

        //BOTON BIEN
   /*
        bien.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LO COMENTO 12/02/2021
                //    Funcionar_salida(1);
                //    Funcionar_salida_Ant(1, 1,0,0);
             //   num_epoch = 1;
                num_epoch = 1;
                int epochs = (int)1;

                float location1 = Float.parseFloat(location.getText().toString());
                float email1 = Float.parseFloat(email.getText().toString());
                float device1 = Float.parseFloat(device.getText().toString());
                float imei1 = Float.parseFloat(imei.getText().toString());

                //  float salida_calculada = Funcionar_salida_Ant(location1,email1,imei1,device1);


              //  train_Ant(location1,email1,device1, imei1,1, salida_calculada);
              //  train_Ant(location1,email1,device1, imei1,100, salida_calculada);ç

                ArrayList<Tensor<?>> salida_calculada = Funcionar_salida_Ant_umbral(location1,email1,imei1,device1);
                float salida_calculada_umbral_verde = salida_calculada.get(0).floatValue();
                float salida_calculada_umbral_naranja =salida_calculada.get(1).floatValue();
                float salida_calculada_umbral_rojo =salida_calculada.get(2).floatValue();

                if(nivel_color == 0) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_verde, umbral_verde);
                }
                if(nivel_color == 1) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_naranja, umbral_naranja);
                }
                if(nivel_color == 2) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_naranja, umbral_rojo);
                }
                Toast.makeText(MainActivity.this, "Perfecto!", Toast.LENGTH_SHORT).show();


               // Funcionar_salida_Ant(location, email,imei,device);
                //prueba

            }
        });


        //BOTON MAL
        mal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LO COMENTO 12/02/2021
                //    Funcionar_salida(1);
                //    Funcionar_salida_Ant(1, 1,0,0);
                //   num_epoch = 1;
                num_epoch = 1;
                int epochs = (int)1;

                float location1 = Float.parseFloat(location.getText().toString());
                float email1 = Float.parseFloat(email.getText().toString());
                float device1 = Float.parseFloat(device.getText().toString());
                float imei1 = Float.parseFloat(imei.getText().toString());

              //  float salida_calculada = Funcionar_salida_Ant(location1,email1,imei1,device1);
                //temporal ??
                ArrayList<Tensor<?>> salida_calculada = Funcionar_salida_Ant_umbral(location1,email1,imei1,device1);
                float salida_calculada_umbral_verde = salida_calculada.get(0).floatValue();
                float salida_calculada_umbral_naranja =salida_calculada.get(1).floatValue();
                float salida_calculada_umbral_rojo =salida_calculada.get(2).floatValue();
                //ajusto umbral_Verde
                if(nivel_color == 0) {
                    if (salida_calculada_umbral_verde == 1) {
                        salida_calculada_umbral_verde = 0;
                        umbral_verde = (float) (umbral_verde + 0.2);

                    } else {
                        salida_calculada_umbral_verde = 0;
                        umbral_verde = (float) (umbral_verde - 0.2);

                    }
                }
                if(nivel_color == 1) {
                    //ajusto umbral_naranja
                    if (salida_calculada_umbral_naranja == 1) {
                        salida_calculada_umbral_naranja = 0;
                        umbral_naranja = (float) (umbral_naranja + 0.2);

                    } else {
                        salida_calculada_umbral_naranja = 0;
                        umbral_naranja = (float) (umbral_naranja - 0.2);

                    }
                }

                if(nivel_color == 2) {
                    //ajusto umbral_rojo
                    if (salida_calculada_umbral_rojo == 1) {
                        salida_calculada_umbral_rojo = 0;
                        umbral_rojo = (float) (umbral_rojo + 0.2);

                    } else {
                        salida_calculada_umbral_rojo = 0;
                        umbral_rojo = (float) (umbral_rojo - 0.2);

                    }
                }




               // train_Ant(location1,email1,device1,imei1,epochs,salida_calculada);
             //   train_Ant(location1,email1,device1,imei1,100,salida_calculada);

                if(nivel_color == 0) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_verde, umbral_verde);
                }
                if(nivel_color == 1) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_naranja, umbral_naranja);
                }
                if(nivel_color == 2) {
                    train_Ant_test(location1, email1, device1, imei1, 1, salida_calculada_umbral_naranja, umbral_rojo);
                }
                num++;
                Graficar_location();
                Graficar_email();
                Graficar_device();
                Graficar_imei();
                Toast.makeText(MainActivity.this, "Seguiremos mejorando, disculpe las molestias", Toast.LENGTH_SHORT).show();

                // Funcionar_salida_Ant(location, email,imei,device);
                //prueba

            }
        });
*/
        //BOTON INFO
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

           //  startActivity(new Intent(MainActivity.this, MasInformacion.class));
                Intent intent = new Intent(MainActivity.this, MasInformacion.class);
                intent.putExtra(PLOCATION, Plocation);
                intent.putExtra(PEMAIL, Pemail);
                intent.putExtra(PDEVICE, Pdevice);
                intent.putExtra(PIMEI, Pimei);
                intent.putExtra(PADVERTISER, Padvertiser);
                intent.putExtra(PSERIALNUMBER, Pserialnumber);
                intent.putExtra(PMACADDRESS, Pmacaddress);
                startActivity(intent);





            }
        });


        //Save Weights in Private Storage
//        finalSave();

        //Upload Weights to Server

        //Check if new model is available



    }

//    public void logWeight(float[] flat) {
//        String s = "";
//        for (int z = 0; z < flat.length / 10; z++) {
//            s += "  " + flat[z];
//        }
//        Log.i("Array: ", s);
//    }




    // funcion bien:
 public  void funcionBien (float location3, float email3, float device3, float imei3, float serialnumber3, float macaddress3, float advertiser3, Context context) {
     num_epoch = 1;
     int epochs = (int) 1;

     float location1 = location3;
     float email1 =  email3;
     float device1 = device3;
     float imei1 =  imei3;
     float serialnumber1 =  serialnumber3;
     float macaddresss1 =  macaddress3;
     float advertiser1 =  advertiser3;


     ArrayList<Tensor<?>> salida_calculada = Funcionar_salida_Ant_umbral(location1, email1, imei1, device1, serialnumber1,macaddresss1,advertiser1);
     float salida_calculada_umbral_verde = salida_calculada.get(0).floatValue();
     float salida_calculada_umbral_naranja = salida_calculada.get(1).floatValue();
     float salida_calculada_umbral_rojo = salida_calculada.get(2).floatValue();

     if (nivel_color == 0) {
         train_Ant_test(location1, email1, device1, imei1, serialnumber1, macaddresss1, advertiser1, 1, salida_calculada_umbral_verde, umbral_verde);
     }
     if (nivel_color == 1) {
         train_Ant_test(location1, email1, device1, imei1, serialnumber1, macaddresss1, advertiser1, 1, salida_calculada_umbral_naranja, umbral_naranja);
     }
     if (nivel_color == 2) {
         train_Ant_test(location1, email1, device1, imei1, serialnumber1, macaddresss1, advertiser1, 1, salida_calculada_umbral_rojo, umbral_rojo);
     }
     Toast.makeText(context, "Perfecto!", Toast.LENGTH_SHORT).show();

 }
 //funcion mal
   public static void funcionMal_static (float location3, float email3, float device3, float imei3, float serialnumber3, float macaddress3, float advertiser3, Context context) {


    int epochs = (int)1;

       float location1 = location3;
       float email1 =  email3;
       float device1 = device3;
       float imei1 =  imei3;
       float serialnumber1 =  serialnumber3;
       float macaddress1 =  macaddress3;
       float advertiser1 =  advertiser3;

    //  float salida_calculada = Funcionar_salida_Ant(location1,email1,imei1,device1);
    //temporal ??


   }

   public static void  test(Context context){
        int i = 0;
        Toast.makeText(context, "Seguiremos mejorando, disculpe las molestias", Toast.LENGTH_SHORT).show();
    }
    // funcion bien:
    public static void funcionBien_static (float location3, float email3, float device3, float imei3, float serialnumber3, float macaddress3, float advertiser3, Context context) {



    }
    // Funcionar_salida_Ant(location, email,imei,device);
    //prueba

    ///*******************************
    //********************************
/// lista de apps analizadas mock data

    private ArrayList<Entidad> GetArrayItems(){
      //  ArrayList<Entidad> listItems = new ArrayList<>();
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));

        return listItems;
    }

    private ArrayList<Entidad> GetArrayItemsAux(){
        ArrayList<Entidad> listItems2 = new ArrayList<>();
        listItems2.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems2.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems2.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems2.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));
        listItems2.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,""));

        return listItems2;
    }

    //***********
  //TRAINING THE MODEL ON-DEVICE
    //*********
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeGraph() {
        checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "final_model.ckpt").toString());
        checkpointDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        graph = new Graph();
        sess = new Session(graph);
        InputStream inputStream;
        try {
            inputStream = getAssets().open("final_graph_hdd.pb");
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            graphDef = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        graph.importGraphDef(graphDef);
        try {
            sess.runner().feed("save/Const", checkpointPrefix).addTarget("save/restore_all").run();
            Toast.makeText(this, "Checkpoint Found and Loaded!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            sess.runner().addTarget("init").run();
            Log.i("Checkpoint: ", "Graph Initialized");
        }
    }
// ***********
//

    //
    //
    private float[] flattenedWeight(Tensor t) {
/*
        float[] flat = new float[(int) (t.shape()[0]) * (int) t.shape()[1]];
       float[][] arr = new float[(int) (t.shape()[0])][(int) t.shape()[1]];
        t.copyTo(arr);
        int x = 0;
        for (int i = 0; i < t.shape()[0]; i++) {
            for (int j = 0; j < t.shape()[1]; j++) {
               flat[x] = arr[i][j];
                x++;
            }
        }
       return flat;

    }
    */
        float[] flat = new float[0];

        t.copyTo(flat);


        return flat;

    }



    //FALTA UNA FUNCION PREDICT PARA ESTIMAR LO QUE SEA QUE HAYA QUE ESTIMAR.
    //MODIFICARLA PARA CADA MODELO QUE SE QUIERA

    private float predict(float[][] features) {
        Tensor input = Tensor.create(features);
        float[][] output = new float[1][1];
        Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        Log.i("Tensor Shape", op_tensor.shape()[0] + ", " + op_tensor.shape()[1]);
        op_tensor.copyTo(output);
        return output[0][0];
    }
    //**************************
    //RUNNING ON DEVICE TRAINING:
    //************************+
    private String train(float[][][] features, float[] label, int epochs){
        org.tensorflow.Tensor x_train = Tensor.create(features);
        Tensor y_train = Tensor.create(label);
        int ctr = 0;
        while (ctr < epochs) {
            sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            ctr++;
        }
        return "Model Trained";
    }
    //SE EXTRAEN LOS WEIGHTS DEL MODELO
    public ArrayList<ArrayList<Tensor<?>>> getWeights() {
        ArrayList<Tensor<?>> w1 = (ArrayList<Tensor<?>>) sess.runner().fetch("Variable:0").run();
        ArrayList<Tensor<?>> b1 = (ArrayList<Tensor<?>>) sess.runner().fetch("Variable_1:0").run();
        ArrayList<Tensor<?>> w2 = (ArrayList<Tensor<?>>) sess.runner().fetch("Variable_2:0").run();
        ArrayList<Tensor<?>> b2 = (ArrayList<Tensor<?>>) sess.runner().fetch("Variable_3:0").run();
        ArrayList<ArrayList<Tensor<?>>> ls = new ArrayList<>();
        ls.add(w1);
        ls.add(b1);
        ls.add(w2);
        ls.add(b2);
        return ls;
    }







    //SE GUARDAN LOS WEIGHTS EN EL DISPOSITIVO
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void finalSave() {
        ArrayList<ArrayList<Tensor<?>>> at = getWeights();

        ArrayList<float[]> diff = new ArrayList<>();
        ArrayList<ArrayList<Tensor<?>>> bt = getWeights();
        for(int x = 0; x < 4; x++)
        {
            float[] d = new float[flattenedWeight(bt.get(x).get(0)).length];
            float[] bw = flattenedWeight(bt.get(x).get(0));
            float[] aw = flattenedWeight(at.get(x).get(0));

            for(int j = 0; j < bw.length; j++)
            {
                d[j] = aw[j] - bw[j];
            }
            diff.add(d);
        }

        save(diff);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save(ArrayList<float[]> diff){


        int l1 = diff.get(0).length;
        int l2 = diff.get(1).length;
        int l3 = diff.get(2).length;
        int l4 = diff.get(3).length;

        float[] result = new float[l1 + l2 + l3 + l4];
        System.arraycopy(diff.get(0), 0, result, 0, l1);
        System.arraycopy(diff.get(1), 0, result, l1, l2);
        System.arraycopy(diff.get(2), 0, result, l2, l3);
        System.arraycopy(diff.get(3), 0, result, l3, l4);
        saveWeights(result, "Weights.bin");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveWeights(float[] diff, String name) {
        byte[] byteArray = new byte[diff.length * 4];
        Log.i("Length of FloatArray: ", String.valueOf(diff.length));

        // wrap the byte array to the byte buffer
        ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);

        // create a view of the byte buffer as a float buffer
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();

        // now put the float array to the float buffer,
        // it is actually stored to the byte array
        floatBuf.put(diff);
        saveFile(byteArray, name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveFile(byte[] byteArray, String name) {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.i("Error: FILE", "File not Created!");
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.i("Error: FILE", "File not found!");
        }
        try {
            os.write(byteArray);
            Log.i("FileWriter", "File written successfully");
        } catch (IOException e) {
            Log.i("Error: FILE", "File not written!");
        }
    }

    ////LAS PRUEBAS DEL BOTON PRUEBA
    //**
    // (i) Loading the model in the Android Application
    //**
    public void initializeGraphPrueba() {
        //path to checkpoit.ckpt HACER
        //To load the checkpoint, place the checkpoint files in the device and create a Tensor
        //to the path of the checkpoint prefix
        //FALTA ******

        // si modelo updated se usa el checkpoints que he descargado del servidor. Si no, el del movil.
        if (isModelUpdated){
         /*   //NO VA BIEN:
            checkpointPrefix = Tensors.create((file_descargado).toString());
            Toast.makeText(MainActivity.this, "Usando el modelo actualizado", Toast.LENGTH_SHORT).show();

          */

         //A VER SI VA
            try {
                //Place the .pb file generated before in the assets folder and import it as a byte[] array
                // hay que poner el .meta
               inputCheck = getAssets().open("+checkpoints_name_1002-.ckpt.meta");
            //    inputCheck = getAssets().open("+checkpoints_name_1002-.ckpt"); NOT FOUND
                byte[] buffer = new byte[inputCheck.available()];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = inputCheck.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                variableAuxCheck = output.toByteArray();  // array con el checkpoint
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkpointPrefix = Tensors.create((variableAuxCheck).toString());
        }

        else {
            try {
                //Place the .pb file generated before in the assets folder and import it as a byte[] array
                // hay que poner el .meta
                inputCheck = getAssets().open("checkpoint_name1.ckpt.meta");
                byte[] buffer = new byte[inputCheck.available()];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = inputCheck.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                variableAuxCheck = output.toByteArray();  // array con el checkpoint
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkpointPrefix = Tensors.create((variableAuxCheck).toString());
        }

       //checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "final_model.ckpt").toString());  //PARA USAR EL CHECKPOINT DESCARGADO
      //  checkpointDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        //Create a variable of class org.tensorflow.Graph:
        graph = new Graph();
        sess = new Session(graph);
        InputStream inputStream;
        try {
       //   inputStream = getAssets().open("graph.pb");  //MODELO SENCILLO      //PROBAR CON GRAPH_PRUEBA QUE ES MI GRAPH DE O BYTES
         //  inputStream = getAssets().open("graph5.pb");  //MODELO SENCILLO
            if (isModelUpdated) {                                                  // ESTO ES ALGO TEMPORAL. NO ES BUENO
                inputStream = getAssets().open("graph_pesos.pb");
            }
            else {
                inputStream = getAssets().open("graph5.pb");
            }
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            graphDef = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Place the .pb file generated before in the assets folder and import it as a byte[] array.
        // Let the array's name be graphdef.
        //Now, load the graph from the graphdef:
        graph.importGraphDef(graphDef);
        try {
            //Now, load the checkpoint by running the restore checkpoint op in the graph:
            sess.runner().feed("save/Const", checkpointPrefix).addTarget("save/restore_all").run();
            Toast.makeText(this, "Checkpoint Found and Loaded!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            //Alternatively, initialize the graph by calling the init op:
            sess.runner().addTarget("init").run();
            Log.i("Checkpoint: ", "Graph Initialized");
        }
    }
   //**
   // (ii) Performing Inference using the model
    // FUNCION QUE USA EL MODLEO PARA OBTENER EL OUTPUT ?
    //**
    //feature en mi caso es num1 que es math.random
   private float predictPrueba(float features) {
        float n_epochs = num_epoch;
       float num1 = (float) Math.random();
       float output = 0; //y

       //First, create an input tensor:
       /*

 */


       //**** TEORIA *******
        //First, create an input tensor:
             //Tensor input = Tensor.create(features);
            // float[][] output = new float[1][1];
       //Then perform inference by:
             //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
       //Copy this output to a float array using:
              //op_tensor.copyTo(output);
          // values.copyTo(output);
       Tensor input = Tensor.create(features);
       Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
       //para escribir en la app en W y B test
      ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
      // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

       Wtest.setText("W_inicial: "+(Float.toString(values.get(0).floatValue())));
       Btest.setText("b_inicial: "+Float.toString(values.get(1).floatValue()));
       y_mejoras_w.add(((values.get(0).floatValue())));
       x_mejoras_w.add( ""+(0+ num_epoch*num));

       y_mejoras_b.add(((values.get(1).floatValue())));
       x_mejoras_b.add( ""+(0+ num_epoch*num));

       ///

      // Y.setText(Float.toString(values.get(1).floatValue()));

       return output; ///mal
   }


    private float Funcionar_salida(float features) {
        float n_epochs = num_epoch;
        float num1 = (float) Math.random();
        float output = 0; //y

        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input = Tensor.create(features);
        Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
        //para escribir en la app en W y B test
        ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");

        ///

        // Y.setText(Float.toString(values.get(1).floatValue()));

        return output; ///mal
    }


    private float Funcionar_salida_Ant(float location, float email, float imei, float device) {
        float n_epochs = 1;

        float output = 0; //y

        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";

        if (location == 1){
            location_filtrado = "SI";
        }
        if (email == 1){
            email_filtrado = "SI";
        }
        if (device == 1){
            device_filtrado = "SI";
        }
        if (imei == 1){
            imei_filtrado = "SI";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);

        Tensor op_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).fetch("output").run().get(0).expect(Float.class);
        //para escribir en la app en W y B test
       // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

       // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
      //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        if (op_tensor.floatValue() == 1) {
            recomendacion = "Revisar App";
        }

        resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
                "Recomendación: " + recomendacion);
        ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();

            y_mejoras_location.add(((values.get(0).floatValue())));
            x_mejoras_location.add("" + (0 + num_epoch*num));

            y_mejoras_email.add(((values.get(1).floatValue())));
            x_mejoras_email.add("" + (0 + num_epoch*num));

            y_mejoras_device.add(((values.get(2).floatValue())));
            x_mejoras_device.add("" + (0 + num_epoch*num));

            y_mejoras_imei.add(((values.get(3).floatValue())));
            x_mejoras_imei.add("" + (0 + num_epoch*num));


        ///

        // Y.setText(Float.toString(values.get(1).floatValue()));

        return op_tensor.floatValue();
    }
  //input umbral : umbral verde, umbral naranaja, umbral rojo
    private ArrayList<Tensor<?>> Funcionar_salida_Ant_umbral(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser) {
        float n_epochs = 1;

        float output = 0; //y

        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);

        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);



        ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";
        String serialnumber_filtrado = "NO";
        String macaddress_filtrado = "NO";
        String advertiser_filtrado = "NO";

        String filtraciones_aplicacion = "";

        if (location == 1){
            location_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Location" +  "\n";
        }
        if (email == 1){
            email_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Email" +  "\n";
        }
        if (device == 1){
            device_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -DeviceID" +  "\n";
        }
        if (imei == 1){
            imei_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Imei" +  "\n";

        }
        if (serialnumber == 1){
            serialnumber_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -SerialNumber" +  "\n";

        }
        if (macaddress == 1){
            macaddress_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -MacAddress" +  "\n";

        }
        if (advertiser == 1){
            advertiser_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -AdvertiserID" +  "\n";

        }

        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
      //umbral_green
        Tensor op_tensor_verde = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("umbral",input_umbral_verde).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral Naranja
        Tensor op_tensor_naranja = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("umbral",input_umbral_naranja).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral rojo
        Tensor op_tensor_rojo =sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("umbral",input_umbral_rojo).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

        list_op_tensor.add(op_tensor_verde);
        list_op_tensor.add(op_tensor_naranja);
        list_op_tensor.add(op_tensor_rojo);

        //para escribir en la app en W y B test
        // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
        //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
        if (op_tensor_verde.floatValue() == 1) {
            if(op_tensor_naranja.floatValue() == 1 ){
                if(op_tensor_rojo.floatValue() == 1 ){
                    Nivel = " Alto";
                    Nivel_color = 2;
                }
                else {
                    Nivel = " Medio";
                    Nivel_color = 1;
                }
            }
            else {
                Nivel = " Bajo";
                Nivel_color = 0;
            }
        }

     ///   test.setBackgroundResource(R.color.holo_green_light);


       // resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
      //          "Recomendación: " + recomendacion);
        subtitulo.setText("Nivel: " );
        titulo.setText(Nivel );
   //     titulo.setTextColor(android.R.color.background_dark);

      //  resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado );
        resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + filtraciones_aplicacion );

        //mirar  bien codigo:
        if ( Nivel_color == 0) {
       //   resumen_app.setBackgroundResource(R.color.verde);
          nivel_color = 0;
          resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
      }
        if ( Nivel_color == 1) {
         //   resumen_app.setBackgroundResource(R.color.naranja);
            nivel_color = 1;
            resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
        }
        if ( Nivel_color == 2) {
          //  resumen_app.setBackgroundResource(R.color.rojo);
            nivel_color = 2;
            resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
        }

     /*  ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();

     //   y_mejoras_location.add(((values.get(0).floatValue())));
        y_mejoras_location.add(Plocation);
        x_mejoras_location.add("" + (0 + num_epoch*num));

      //  y_mejoras_email.add(((values.get(1).floatValue())));
        y_mejoras_email.add(Pemail);
        x_mejoras_email.add("" + (0 + num_epoch*num));

       // y_mejoras_device.add(((values.get(2).floatValue())));
        y_mejoras_device.add(Pdevice);
        x_mejoras_device.add("" + (0 + num_epoch*num));

        //y_mejoras_imei.add(((values.get(3).floatValue())));
        y_mejoras_imei.add(Pimei);
        x_mejoras_imei.add("" + (0 + num_epoch*num));


      */

        ///

        // Y.setText(Float.toString(values.get(1).floatValue()));

        return list_op_tensor;
    }
    //// PARA LAS LISTAS:

    //input umbral : umbral verde, umbral naranaja, umbral rojo
    private String Funcionar_salida_Ant_umbral_lista_Dentro(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser) {
        float n_epochs = 1;

        float output = 0; //y
        String op_string;


        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);

        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);



        ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";
        String serialnumber_filtrado = "NO";
        String macaddress_filtrado = "NO";
        String advertiser_filtrado = "NO";

        String filtraciones_aplicacion = "";

        if (location == 1){
            location_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Location" +  "\n";
        }
        if (email == 1){
            email_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Email" +  "\n";
        }
        if (device == 1){
            device_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -DeviceID" +  "\n";
        }
        if (imei == 1){
            imei_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Imei" +  "\n";

        }
        if (serialnumber == 1){
            serialnumber_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -SerialNumber" +  "\n";

        }
        if (macaddress == 1){
            macaddress_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -MacAddress" +  "\n";

        }
        if (advertiser == 1){
            advertiser_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -AdvertiserID" +  "\n";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
        //umbral_green
        Tensor op_tensor_verde = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_verde).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral Naranja
        Tensor op_tensor_naranja = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_naranja).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral rojo
        Tensor op_tensor_rojo =sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_rojo).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

        list_op_tensor.add(op_tensor_verde);
        list_op_tensor.add(op_tensor_naranja);
        list_op_tensor.add(op_tensor_rojo);

        //para escribir en la app en W y B test
        // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
        //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
        if (op_tensor_verde.floatValue() == 1) {
            if(op_tensor_naranja.floatValue() == 1 ){
                if(op_tensor_rojo.floatValue() == 1 ){
                    Nivel = " Alto";
                    Nivel_color = 2;
                }
                else {
                    Nivel = " Medio";
                    Nivel_color = 1;
                }
            }
            else {
                Nivel = " Bajo";
                Nivel_color = 0;
            }
        }

        ///   test.setBackgroundResource(R.color.holo_green_light);


        // resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
        //          "Recomendación: " + recomendacion);

       // ************************* COMENTADO*********18/02

        //       subtitulo.setText("Nivel: " );
 //       titulo.setText(Nivel );
        //     titulo.setTextColor(android.R.color.background_dark);

        //  resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado );
 //  **********************
        //       resumen_app.setText("Filtraciones: "+ "\n" +  "\n" + filtraciones_aplicacion );
        op_string = "Filtraciones: "+ "\n" +  "\n" + filtraciones_aplicacion;

        //mirar  bien codigo:
        if ( Nivel_color == 0) {
            //   resumen_app.setBackgroundResource(R.color.verde);
            nivel_color = 0;
    //        resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
        }
        if ( Nivel_color == 1) {
            //   resumen_app.setBackgroundResource(R.color.naranja);
            nivel_color = 1;
   //         resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
        }
        if ( Nivel_color == 2) {
            //  resumen_app.setBackgroundResource(R.color.rojo);
            nivel_color = 2;
    //        resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
        }




        return op_string;
    }

    private String Funcionar_salida_Ant_umbral_lista_Nivel(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser) {
        float n_epochs = 1;

        float output = 0; //y
        String op_string;


        //First, create an input tensor:
        /*

         */



        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);

        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);



        ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";
        String serialnumber_filtrado = "NO";
        String macaddress_filtrado = "NO";
        String advertiser_filtrado = "NO";
        String filtraciones_aplicacion = "";

        if (location == 1){
            location_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Location" +  "\n";
        }
        if (email == 1){
            email_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Email" +  "\n";
        }
        if (device == 1){
            device_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -DeviceID" +  "\n";
        }
        if (imei == 1){
            imei_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Imei" +  "\n";

        }
        if (serialnumber == 1){
            serialnumber_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -SerialNumber" +  "\n";

        }
        if (macaddress == 1){
            macaddress_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -MacAddress" +  "\n";

        }
        if (advertiser == 1){
            advertiser_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -AdvertiserID" +  "\n";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
        //umbral_green
        //umbral_green
        Tensor op_tensor_verde = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_verde).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral Naranja
        Tensor op_tensor_naranja = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_naranja).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral rojo
        Tensor op_tensor_rojo =sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_rojo).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

        list_op_tensor.add(op_tensor_verde);
        list_op_tensor.add(op_tensor_naranja);
        list_op_tensor.add(op_tensor_rojo);

        //para escribir en la app en W y B test
        // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
        //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
        if (op_tensor_verde.floatValue() == 1) {
            if(op_tensor_naranja.floatValue() == 1 ){
                if(op_tensor_rojo.floatValue() == 1 ){
                    Nivel = " Alto";
                    Nivel_color = 2;
                }
                else {
                    Nivel = " Medio";
                    Nivel_color = 1;
                }
            }
            else {
                Nivel = " Bajo";
                Nivel_color = 0;
            }
        }

        ///   test.setBackgroundResource(R.color.holo_green_light);


        // resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
        //          "Recomendación: " + recomendacion);
      //  subtitulo.setText("Nivel: " );
      //  titulo.setText(Nivel );
        //     titulo.setTextColor(android.R.color.background_dark);

        //  resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado );
    //    resumen_app.setText("Filtraciones: "+ "\n" +  "\n" + filtraciones_aplicacion );
        op_string = Nivel;

        //mirar  bien codigo:
        if ( Nivel_color == 0) {
            //   resumen_app.setBackgroundResource(R.color.verde);
            nivel_color = 0;
   //         resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
        }
        if ( Nivel_color == 1) {
            //   resumen_app.setBackgroundResource(R.color.naranja);
            nivel_color = 1;
      //      resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
        }
        if ( Nivel_color == 2) {
            //  resumen_app.setBackgroundResource(R.color.rojo);
            nivel_color = 2;
      //      resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
        }




        return op_string;
    }



    //input umbral : umbral verde, umbral naranaja, umbral rojo
    private int Funcionar_salida_Ant_umbral_Color(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser) {
        float n_epochs = 1;

        float output = 0; //y
        String op_string;


        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);

        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);



        ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";
        String serialnumber_filtrado = "NO";
        String macaddress_filtrado = "NO";
        String advertiser_filtrado = "NO";
        String filtraciones_aplicacion = "";

        if (location == 1){
            location_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Location" +  "\n";
        }
        if (email == 1){
            email_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Email" +  "\n";
        }
        if (device == 1){
            device_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -DeviceID" +  "\n";
        }
        if (imei == 1){
            imei_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -Imei" +  "\n";

        }
        if (serialnumber == 1){
            serialnumber_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -SerialNumber" +  "\n";

        }
        if (macaddress == 1){
            macaddress_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -MacAddress" +  "\n";

        }
        if (advertiser == 1){
            advertiser_filtrado = "SI";
            filtraciones_aplicacion = filtraciones_aplicacion + " -AdvertiserID" +  "\n";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
        //umbral_green
        //umbral_green
        Tensor op_tensor_verde = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_verde).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral Naranja
        Tensor op_tensor_naranja = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_naranja).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
        //umbral rojo
        Tensor op_tensor_rojo =sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_rojo).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

        list_op_tensor.add(op_tensor_verde);
        list_op_tensor.add(op_tensor_naranja);
        list_op_tensor.add(op_tensor_rojo);

        //para escribir en la app en W y B test
        // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
        //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
        if (op_tensor_verde.floatValue() == 1) {
            if(op_tensor_naranja.floatValue() == 1 ){
                if(op_tensor_rojo.floatValue() == 1 ){
                    Nivel = " Alto";
                    Nivel_color = 2;
                }
                else {
                    Nivel = " Medio";
                    Nivel_color = 1;
                }
            }
            else {
                Nivel = " Bajo";
                Nivel_color = 0;
            }
        }

        ///   test.setBackgroundResource(R.color.holo_green_light);


        // resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
        //          "Recomendación: " + recomendacion);
    //    subtitulo.setText("Nivel: " );
     //   titulo.setText(Nivel );
        //     titulo.setTextColor(android.R.color.background_dark);

        //  resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado );
     //   resumen_app.setText("Filtraciones: "+ "\n" +  "\n" + filtraciones_aplicacion );
        op_string = "Filtraciones: "+ "\n" +  "\n" + filtraciones_aplicacion;

        //mirar  bien codigo:
        if ( Nivel_color == 0) {
            //   resumen_app.setBackgroundResource(R.color.verde);
            nivel_color = 0;
      //      resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
        }
        if ( Nivel_color == 1) {
            //   resumen_app.setBackgroundResource(R.color.naranja);
            nivel_color = 1;
        //    resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
        }
        if ( Nivel_color == 2) {
            //  resumen_app.setBackgroundResource(R.color.rojo);
            nivel_color = 2;
         //   resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
        }



        return nivel_color;
    }





    private ArrayList<Float> Funcionar_salida_Ant_umbral_recogidaDatos(float location, float email, float imei, float device, float umbral) {
        float n_epochs = 1;

        float output = 0; //y
        ArrayList<Float> recogidaDatos = new ArrayList<>();

        //First, create an input tensor:
        /*

         */


        //**** TEORIA *******
        //First, create an input tensor:
        //Tensor input = Tensor.create(features);
        // float[][] output = new float[1][1];
        //Then perform inference by:
        //Tensor op_tensor = sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class);
        //Copy this output to a float array using:
        //op_tensor.copyTo(output);
        // values.copyTo(output);
        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);

        Tensor input_umbral = Tensor.create(umbral);

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";

        if (location == 1){
            location_filtrado = "SI";
        }
        if (email == 1){
            email_filtrado = "SI";
        }
        if (device == 1){
            device_filtrado = "SI";
        }
        if (imei == 1){
            imei_filtrado = "SI";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);

        Tensor op_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("umbral",input_umbral).fetch("output").run().get(0).expect(Float.class);


        //para escribir en la app en W y B test
        // LO COMENTO 12/02/2021
        // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
        // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

        //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

        // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
        //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

        String recomendacion = "No hacer nada";
        if (op_tensor.floatValue() == 1) {
            recomendacion = "Revisar App";
        }
//CAMBIAR
/*
        ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();

        recogidaDatos.add(0,values.get(0).floatValue());
        recogidaDatos.add(1,values.get(1).floatValue());
        recogidaDatos.add(2,values.get(2).floatValue());
        recogidaDatos.add(3,values.get(3).floatValue());
        ///

        // Y.setText(Float.toString(values.get(1).floatValue()));


 */
        return recogidaDatos;
    }


    //SI MODELO Y= M*X + C
   //EN INPUT= X_TRAIN SE PONE EL PARAMETRO DE ENTRADA, QUE ES X.(ver el otro ejemplo de regresion)
   //EN TARGET=Y_TRAIN, QUE ES EN ESTE CASO Y= M*X + C(ver ejemplo)
    //con session.runner.feed.... se usa el modelo.
    //private String trainPrueba(float features, float[] label, int epochs){
   private String trainPrueba(float features, int epochs){

/*
       // PRIMER INTENTO
        //First, create the tensors for the input and the labels:
        org.tensorflow.Tensor x_train = Tensor.create(features); //input
        Tensor y_train = Tensor.create(10*features + 2);         //output
       // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:
        int ctr = 0;
        while (ctr < epochs) {


           // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

            ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
            W.setText((Float.toString(values.get(0).floatValue())));
            B.setText(Float.toString(values.get(1).floatValue()));
            ctr++;
        }
        return "Model Trained";
    }

    //
*/
       //First, create the tensors for the input and the labels:

       // Tensor y_train = Tensor.create(label);                  //output
       //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:
       Random random = new Random();
       int ctr = 0;
       int h = epochs - 2;
       while (ctr < epochs) {
           float in = random.nextFloat();

           org.tensorflow.Tensor x_train = Tensor.create(in); //input
           Tensor y_train = Tensor.create(10*in + 2);         //output


         //   sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
           sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

           ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
           W.setText("W_final: "+(Float.toString(values.get(0).floatValue())));
           B.setText("b_final: "+Float.toString(values.get(1).floatValue()));
           ctr++;

           if (ctr == h) {
             //  pesos.add((Float.toString(values.get(0).floatValue())));
             //  pesos.add((Float.toString(values.get(1).floatValue())));
               pesos.add(0,(Float.toString(values.get(0).floatValue())));
               Log.i("valor 0: ", pesos.get(0));
               pesos.add(1,(Float.toString(values.get(1).floatValue())));
               Log.i("valor 1: ", pesos.get(1));


           }
       }
       return "Model Trained";
   }
// ENTRENAR ANT MONITOR:
private String train_Ant(float location, float email, float device, float imei, int epochs, float target){


    //First, create the tensors for the input and the labels:

    // Tensor y_train = Tensor.create(label);                  //output
    //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:
    Random random = new Random();
    int ctr = 0;
    int h = epochs - 2;
 //   int h = epochs;
    while (ctr < epochs) {
        float in = random.nextFloat();

        org.tensorflow.Tensor location_train = Tensor.create(location); //input
        org.tensorflow.Tensor email_train = Tensor.create(email); //input
        org.tensorflow.Tensor device_train = Tensor.create(device); //input
        org.tensorflow.Tensor imei_train = Tensor.create(imei); //input

        Tensor y_train = Tensor.create(target);         //output, target


        //   sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
       // VERSION LINNEAR
        // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

        sess.runner().feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("target", y_train).addTarget("train").run();
        //Toast.makeText(this, "Modelo entrenado correctamente!", Toast.LENGTH_SHORT).show();
        ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();
        ctr++;
     // VA A HABER QUE MODIFICARLO:

        if (ctr == h) {
            //  pesos.add((Float.toString(values.get(0).floatValue())));
            //  pesos.add((Float.toString(values.get(1).floatValue())));
            pesos.add(0, (Float.toString(values.get(0).floatValue())));
            Log.i("valor 0: ", pesos.get(0));
            pesos.add(1, (Float.toString(values.get(1).floatValue())));
            Log.i("valor 1: ", pesos.get(1));
            pesos.add(2, (Float.toString(values.get(2).floatValue())));
            Log.i("valor 0: ", pesos.get(2));
            pesos.add(3, (Float.toString(values.get(3).floatValue())));
            Log.i("valor 1: ", pesos.get(3));
        }


    }
    return "Model Trained";
}


    // ENTRENAR ANT MONITOR:
    private String train_Ant_test(float location, float email, float device, float imei, float serialnumber, float macaddresss, float advertiser, int epochs, float target, float umbral){


        //First, create the tensors for the input and the labels:

        // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:

        int ctr = 0;
        int h = epochs - 2;
        //   int h = epochs;
        while (ctr < epochs) {

            org.tensorflow.Tensor location_train = Tensor.create(location); //input
            org.tensorflow.Tensor email_train = Tensor.create(email); //input
            org.tensorflow.Tensor device_train = Tensor.create(device); //input
            org.tensorflow.Tensor imei_train = Tensor.create(imei); //input
            org.tensorflow.Tensor serialnumber_train = Tensor.create(serialnumber); //input
            org.tensorflow.Tensor macaddress_train = Tensor.create(macaddresss); //input
            org.tensorflow.Tensor advertiser_train = Tensor.create(advertiser); //input

            org.tensorflow.Tensor umbral_train = Tensor.create(umbral); //input


            Tensor input_Plocation = Tensor.create(Plocation);
            Tensor input_Pemail = Tensor.create(Pemail);
            Tensor input_Pdevice = Tensor.create(Pdevice);
            Tensor input_Pimei = Tensor.create(Pimei);
            Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
            Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
            Tensor input_Padvertiser = Tensor.create(Padvertiser);


            Tensor y_train = Tensor.create(target);         //output, target


            //   sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            // VERSION LINNEAR
            // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

         //   sess.runner().feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("umbral",umbral_train).feed("target", y_train).addTarget("train").run();
            //umbral_green
            Tensor op_tensor = sess.runner().feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("umbral",umbral_train).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

            //Toast.makeText(this, "Modelo entrenado correctamente!", Toast.LENGTH_SHORT).show();
       //MODIFICAR:


   //         ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();

            // VA A HABER QUE MODIFICARLO:
/*
            if (ctr == h) {
                //  pesos.add((Float.toString(values.get(0).floatValue())));
                //  pesos.add((Float.toString(values.get(1).floatValue())));
                pesos.add(0, (Float.toString(values.get(0).floatValue())));
                Log.i("valor 0: ", pesos.get(0));
                pesos.add(1, (Float.toString(values.get(1).floatValue())));
                Log.i("valor 1: ", pesos.get(1));
                pesos.add(2, (Float.toString(values.get(2).floatValue())));
                Log.i("valor 0: ", pesos.get(2));
                pesos.add(3, (Float.toString(values.get(3).floatValue())));
                Log.i("valor 1: ", pesos.get(3));
                Log.i("valor 1: ", String.valueOf(umbral));

            }

 */

            ctr++;
        }
        return "Model Trained";
    }


    // ENTRENAR ANT MONITOR:
   public static String train_Ant_test_static(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser,float umbral , int epochs, float target){


        //First, create the tensors for the input and the labels:

        // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:

        int ctr = 0;
        int h = epochs - 2;
        //   int h = epochs;
        while (ctr < epochs) {

            org.tensorflow.Tensor location_train = Tensor.create(location); //input
            org.tensorflow.Tensor email_train = Tensor.create(email); //input
            org.tensorflow.Tensor device_train = Tensor.create(device); //input
            org.tensorflow.Tensor imei_train = Tensor.create(imei); //input
            org.tensorflow.Tensor serialnumber_train = Tensor.create(serialnumber); //input
            org.tensorflow.Tensor macaddress_train = Tensor.create(macaddress); //input
            org.tensorflow.Tensor advertiser_train = Tensor.create(advertiser); //input

            org.tensorflow.Tensor umbral_train = Tensor.create(umbral); //input


            Tensor input_Plocation = Tensor.create(Plocation);
            Tensor input_Pemail = Tensor.create(Pemail);
            Tensor input_Pdevice = Tensor.create(Pdevice);
            Tensor input_Pimei = Tensor.create(Pimei);
            Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
            Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
            Tensor input_Padvertiser = Tensor.create(Padvertiser);


            Tensor y_train = Tensor.create(target);         //output, target


            //   sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            // VERSION LINNEAR
            // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

            //   sess.runner().feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("umbral",umbral_train).feed("target", y_train).addTarget("train").run();
            //umbral_green
            Tensor op_tensor = sess.runner().feed("serialnumber_input",serialnumber_train).feed("macaddress_input",macaddress_train).feed("advertiser_input",advertiser_train).feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("umbral",umbral_train).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);
            //Toast.makeText(this, "Modelo entrenado correctamente!", Toast.LENGTH_SHORT).show();
            //MODIFICAR:


            //         ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pdevice/read").fetch("Pimei/read").run();

            // VA A HABER QUE MODIFICARLO:
/*
            if (ctr == h) {
                //  pesos.add((Float.toString(values.get(0).floatValue())));
                //  pesos.add((Float.toString(values.get(1).floatValue())));
                pesos.add(0, (Float.toString(values.get(0).floatValue())));
                Log.i("valor 0: ", pesos.get(0));
                pesos.add(1, (Float.toString(values.get(1).floatValue())));
                Log.i("valor 1: ", pesos.get(1));
                pesos.add(2, (Float.toString(values.get(2).floatValue())));
                Log.i("valor 0: ", pesos.get(2));
                pesos.add(3, (Float.toString(values.get(3).floatValue())));
                Log.i("valor 1: ", pesos.get(3));
                Log.i("valor 1: ", String.valueOf(umbral));

            }

 */

            ctr++;
        }
        return "Model Trained";
    }



    ///////////////////
    private String trainPrueba900(float features, int epochs){


       // PRIMER INTENTO
        //First, create the tensors for the input and the labels:
        org.tensorflow.Tensor x_train = Tensor.create(features); //input
        Tensor y_train = Tensor.create(10*features + 2);         //output
       // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:
        int ctr = 0;
        while (ctr < epochs) {


           // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

            ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
            W.setText((Float.toString(values.get(0).floatValue())));
            B.setText(Float.toString(values.get(1).floatValue()));
            ctr++;
        }
        return "Model Trained";
    }

    //
/*
        //First, create the tensors for the input and the labels:

        // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the ‘train_op’ graph operation defined in the graph to train the graph:
        Random random = new Random();
        int ctr = 0;
        while (ctr < epochs) {
            float in = random.nextFloat();

            org.tensorflow.Tensor x_train = Tensor.create(in); //input
            Tensor y_train = Tensor.create(10*in + 2);         //output


            // sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train_op").run();
            sess.runner().feed("input", x_train).feed("target", y_train).addTarget("train").run();

            ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
            W.setText((Float.toString(values.get(0).floatValue())));
            B.setText(Float.toString(values.get(1).floatValue()));
            ctr++;
        }
        return "Model Trained";
    }
    */

    //**
    //C. Extracting weights from the on-device model
    //**
    public ArrayList<ArrayList<Tensor<?>>> getWeightsPrueba() {
        ArrayList<Tensor<?>> w1 = (ArrayList<Tensor<?>>) sess.runner().fetch("W").run();
        ArrayList<Tensor<?>> b1 = (ArrayList<Tensor<?>>) sess.runner().fetch("b").run();
        ArrayList<ArrayList<Tensor<?>>> ls = new ArrayList<>();
        ls.add(w1);
        ls.add(b1);
      //  Log.i("Shapes: ", w1.get(0).shape()[0] + ", " + w1.get(0).shape()[1]);   PROBLEMA: NO EXISTE W1.GET(0).SHAPE()[1]
      //  Log.i("Shapes: ", b1.get(0).shape()[0] + ", " + b1.get(0).shape()[1]);
        Log.i("Shapes: ", String.valueOf(w1.get(0).shape()));  //CON EL [0] FALLA PORQUE NO EXISTE ???
        Log.i("Shapes: ", String.valueOf(b1.get(0).shape()));
        Log.i("Valor: ", String.valueOf(b1.get(0)));
        return ls;
    }

    //////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeFileLinear() {
        textFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
        try{
            FileOutputStream fos = new FileOutputStream(textFile);
            String mensaje = "esto es un mensaje de prueba \n";
            String mensaje_final = mensaje + "\n" + pesos.get(0) + "\n" + pesos.get(1);
            Log.i("valor 0 final: ", pesos.get(0));
            Log.i("valor 1 final: ", pesos.get(1));
            fos.write(mensaje_final.getBytes());
            fos.close();
            file = textFile;
            Log.i("Error: FILE", "Fichero con msg creado!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////

    //si se hace lo de los grupos, sería GRUPO A: file1, GRUPO B: file2, GRUPO C: file3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeFileAnt() {
        textFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
        try{
            FileOutputStream fos = new FileOutputStream(textFile);
            String mensaje = "Pesos modelo Ant \n";
            String mensaje_final = mensaje + "\n" + Plocation + "\n" + Pemail + "\n" + Pdevice + "\n" + Pimei+ "\n" + Pserialnumber + "\n" + Pmacaddress + "\n" + Padvertiser + "\n" +umbral_verde + "\n" + umbral_naranja + "\n" + umbral_rojo;

            fos.write(mensaje_final.getBytes());
            fos.close();
            file = textFile;
            Log.i("Error: FILE", "Fichero con msg creado!");
            if (clasificacion_usuarios == "A"){
                fileA = textFile;
                Log.i("Error: FILE", "Fichero con msg creado!");
            }
            if (clasificacion_usuarios == "B"){
                fileB = textFile;
                Log.i("Error: FILE", "Fichero con msg creado!");
            }
            if (clasificacion_usuarios == "C"){
                fileC = textFile;
                Log.i("Error: FILE", "Fichero con msg creado!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////




    /////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////

    //si se hace lo de los grupos, sería GRUPO A: file1, GRUPO B: file2, GRUPO C: file3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void readFileAnt() {
        file_leer = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file_leer)));
            String titulo1 = fin.readLine();
            String vacio1 = fin.readLine();
            String Plocation1 = fin.readLine();
            String Pemail1 = fin.readLine();
            String Pdevice1 = fin.readLine();
            String Pimei1 = fin.readLine();
            String Pserialnumber1 = fin.readLine();
            String Pmacaddress1 = fin.readLine();
            String Padvertiser1 = fin.readLine();
            String umbralverde1 = fin.readLine();
            String umbralnaranja1 = fin.readLine();
            String umbralrojo1 = fin.readLine();


            fin.close();
          // double test2 = Integer.parseInt(Pimei);
            // saco Plocation
            float Plocation2 =  Plocation1.charAt(0)  - '0';;
            try { if (Plocation1.charAt(1) != '.'){
                float A1 = Plocation1.charAt(1) - '0';
                float A2 = Plocation1.charAt(0) - '0';
                A2 *= 10;
                Plocation2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Plocation = Plocation2;

            // saco Pemail
            float Pemail2 =  Pemail1.charAt(0)  - '0';;
            try { if (Pemail1.charAt(1) != '.'){
                float A1 = Pemail1.charAt(1) - '0';
                float A2 = Pemail1.charAt(0) - '0';
                A2 *= 10;
                Pemail2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pemail = Pemail2;

            // saco Pdevice
            float Pdevice2 =  Pdevice1.charAt(0)  - '0';;
            try { if (Pdevice1.charAt(1) != '.'){
                float A1 = Pdevice1.charAt(1) - '0';
                float A2 = Pdevice1.charAt(0) - '0';
                A2 *= 10;
                Pdevice2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pdevice = Pdevice2;

            // saco Pimei
            float Pimei2 =  Pimei1.charAt(0)  - '0';;
            try { if (Pimei1.charAt(1) != '.'){
                float A1 = Pimei1.charAt(1) - '0';
                float A2 = Pimei1.charAt(0) - '0';
                A2 *= 10;
                Pimei2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pimei = Pimei2;

            // saco Pserial
            float Pserialnumber2 =  Pserialnumber1.charAt(0)  - '0';;
            try { if (Pserialnumber1.charAt(1) != '.'){
                float A1 = Pserialnumber1.charAt(1) - '0';
                float A2 = Pserialnumber1.charAt(0) - '0';
                A2 *= 10;
                Pserialnumber2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pserialnumber = Pserialnumber2;

            // saco Pmac
            float Pmacaddress2 =  Pmacaddress1.charAt(0)  - '0';;
            try { if (Pmacaddress1.charAt(1) != '.'){
                float A1 = Pmacaddress1.charAt(1) - '0';
                float A2 = Pmacaddress1.charAt(0) - '0';
                A2 *= 10;
                Pmacaddress2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pmacaddress = Pmacaddress2;

            // saco Padver
            float Padvertiser2 =  Padvertiser1.charAt(0)  - '0';;
            try { if (Padvertiser1.charAt(1) != '.'){
                float A1 = Padvertiser1.charAt(1) - '0';
                float A2 = Padvertiser1.charAt(0) - '0';
                A2 *= 10;
                Padvertiser2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Padvertiser = Padvertiser2;

            // saco Padver
            float umbralverde2 =  umbralverde1.charAt(0)  - '0';;
            try { if (Padvertiser1.charAt(1) != '.'){
                float A1 = umbralverde1.charAt(1) - '0';
                float A2 = umbralverde1.charAt(0) - '0';
                A2 *= 10;
               umbralverde2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            umbral_verde = umbralverde2;

            // saco Padver
            float umbralnaranja2 =  umbralnaranja1.charAt(0)  - '0';;
            try { if (Padvertiser1.charAt(1) != '.'){
                float A1 = umbralnaranja1.charAt(1) - '0';
                float A2 = umbralnaranja1.charAt(0) - '0';
                A2 *= 10;
                umbralnaranja2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            umbral_naranja = umbralnaranja2;

            // saco Padver
            float umbralrojo2 =  umbralrojo1.charAt(0)  - '0';;
            try { if (Padvertiser1.charAt(1) != '.'){
                float A1 = umbralrojo1.charAt(1) - '0';
                float A2 = umbralrojo1.charAt(0) - '0';
                A2 *= 10;
                umbralrojo2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            umbral_rojo = umbralrojo2;




            Log.i("Ver contenido ", "Se lee: "+titulo1+vacio1+Plocation1+Pemail1+Pdevice1+Pimei1+Pdevice1+Pimei1+Pserialnumber1+Pmacaddress1+Padvertiser1);
            Log.i("Ver contenido ", "Se lee: "+titulo1+vacio1+Plocation2+Pemail2+Pdevice2+Pimei2+Pdevice2+Pimei2+Pserialnumber2+Pmacaddress2+Padvertiser2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    //SE GUARDAN LOS WEIGHTS EN EL DISPOSITIVO

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void finalSave1() {
        ArrayList<ArrayList<Tensor<?>>> at = getWeightsPrueba();
        int ctr = 0;
        ArrayList<float[]> diff = new ArrayList<>();
        ArrayList<ArrayList<Tensor<?>>> bt = getWeightsPrueba();
        for (int x = 0; x < 2; x++) {   ///vale 2 porque solo w1 y b1
            ArrayList<Tensor<?>> u1 = at.get(x); //para pruebas
            Tensor<?> u = at.get(x).get(0); //variable auxiliar para pruebas
            float[] d = new float[flattenedWeight(bt.get(x).get(0)).length];
            float[] bw = flattenedWeight(bt.get(x).get(0));
            float[] aw = flattenedWeight(at.get(x).get(0));

            diff.add(aw);

            // float aux = at.get(x).get(0);
            // diff.add(at.get(x).get(0));
            for(int j = 0; j < bw.length; j++)
            {
                d[j] = aw[j] - bw[j];
            }
            diff.add(d);
        }
        Log.i("COUNTER: ", String.valueOf(ctr));
        savePrueba1(diff);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void finalSave_linear() {

        ArrayList<float[]> diff = new ArrayList<>();
        float [] h1 = new float[8];
         float [] h2 = new float[2];
            diff.add(h1);
        diff.add(h2);

        savePrueba1(diff);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savePrueba1MIO(ArrayList<float[]> diff){
        ArrayList<ArrayList<Tensor<?>>> at = getWeightsPrueba();
        float[] d1 = diff.get(0);
        float[] d2 = diff.get(1);

        int l1 = diff.get(0).length;
        int l2 = diff.get(1).length;

        int ctr = 0;
        int i = 0;
        int j = 0;
        float[] result = new float[l1 + l2 ];
        for(i = 0, j = 0; j < l1; i++, j++)
        {
            result[i] = d1[j];
        }
        for(int k = 0; k < l2; i++, k++)
        {
            result[i] = d2[k];
        }

        for(float x: result)
        {
            if(x == 0.0)
                ctr++;
        }
        Log.i("COUNTER_A: ", String.valueOf(ctr));
        Log.i("Result Length:  ", String.valueOf(ctr));

        saveWeightsPrueba1(result, "WeightsPrueba.bin");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savePrueba1(ArrayList<float[]> diff){

        float[] d1 = diff.get(0);
        float[] d2 = diff.get(1);

        int l1 = diff.get(0).length;
        int l2 = diff.get(1).length;

        int ctr = 0;
        int i = 0;
        int j = 0;
        float[] result = new float[l1 + l2 ];
        for(i = 0, j = 0; j < l1; i++, j++)
        {
            result[i] = d1[j];
        }
        for(int k = 0; k < l2; i++, k++)
        {
            result[i] = d2[k];
        }

        for(float x: result)
        {
            if(x == 0.0)
                ctr++;
        }
        Log.i("COUNTER_A: ", String.valueOf(ctr));
        Log.i("Result Length:  ", String.valueOf(ctr));

        saveWeightsPrueba1(result, "WeightsPrueba.bin");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveWeightsPrueba1(float[] diff, String name) {
        byte[] byteArray = new byte[diff.length * 4]; // es cuatro pero no se por que
        // wrap the byte array to the byte buffer
        ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);
        for(byte b: byteBuf.array())
        {
            Log.i("ByteBuffer: ", String.valueOf(b));

        }
        // create a view of the byte buffer as a float buffer
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();


        // now put the float array to the float buffer,
        // it is actually stored to the byte array
        floatBuf.put(diff);
        saveFilePrueba1(byteArray, name);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveFilePrueba1(byte[] byteArray, String name) {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.i("Error: FILE", "File not Created!");
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.i("Error: FILE", "File not found!");
        }
        try {
            os.write(byteArray);
            Log.i("FileWriter", "File written successfully");
        } catch (IOException e) {
            Log.i("Error: FILE", "File not written!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savePrueba(ArrayList<float[]> diff){


        int l1 = diff.get(0).length;
        int l2 = diff.get(1).length;


        float[] resultPrueba = new float[l1 + l2 ];
        System.arraycopy(diff.get(0), 0, resultPrueba, 0, l1);
        System.arraycopy(diff.get(1), 0, resultPrueba, l1, l2);


        saveWeightsPrueba(resultPrueba, "WeightsPrueba.bin");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveWeightsPrueba(float[] diffPrueba, String name) {
        byte[] byteArray = new byte[diffPrueba.length * 2];  //multiplico por 2
        Log.i("Length of FloatArray: ", String.valueOf(diffPrueba.length));

        // wrap the byte array to the byte buffer
        ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);

        // create a view of the byte buffer as a float buffer
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();

        // now put the float array to the float buffer,
        // it is actually stored to the byte array
        floatBuf.put(diffPrueba);
        saveFilePrueba(byteArray, name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveFilePrueba(byte[] byteArray, String name) {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.i("Error: FILE", "File not Created!");
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.i("Error: FILE", "File not found!");
        }
        try {
            os.write(byteArray);
            Log.i("FileWriter", "File written successfully");
        } catch (IOException e) {
            Log.i("Error: FILE", "File not written!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeFileTest() {
       textFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
       try{
           FileOutputStream fos = new FileOutputStream(textFile);
           String mensaje = "esto es un mensaje de prueba";
           fos.write(mensaje.getBytes());
           fos.close();
           file = textFile;
           Log.i("Error: FILE", "Fichero con msg creado!");
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeGraph_check() {
   /*    // checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "checkpoint").toString());
        checkpointPrefix = Tensors.create(String.valueOf(getAssets().open("checkpoint")));



    */
     //   checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/checkpoint").toString());
     //   checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/-checkpoints_name_1002-.ckpt").toString());  //FUNCIONABA
    //    checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/checkpoint_actualizado.ckpt").toString()); // FUNCIONA A MEDIAS

        //voy a cambiarlo por ANT: luego tocará cambiarlo
         checkpointPrefix = Tensors.create((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/checkpoint_actualizado_"+modelctr+".ckpt").toString());
         modelctr++;
         //pongo el umbral al nivel normal, una vez entrenado el modelo
         umbral = 10;
        checkpointDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        graph = new Graph();
        sess = new Session(graph);
        InputStream inputStream;
        try {
            //lo cambio por ANT
           // inputStream = getAssets().open("graph5.pb"); //BUENO
           inputStream = getAssets().open("graph_Ant_v13.pb"); //ANT
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            graphDef = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        graph.importGraphDef(graphDef);
        try {
            sess.runner().feed("save/Const", checkpointPrefix).addTarget("save/restore_all").run();
            Toast.makeText(this, "Checkpoint Found and Loaded!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            sess.runner().addTarget("init").run();
            Log.i("Checkpoint: ", "Graph Initialized");
        }
    }



/*
 if (salida_calculada_umbral_verde ==1){
        salida_calculada_umbral_verde = 0;
        umbral_verde = (float) (umbral_verde + 0.2);
        if (location1 ==1){

            if (Plocation <= 1){
                Plocation = 1;
            }
            else Plocation = (float) (Plocation - 0.5);

        }
        if (email1 ==1){

            if (Pemail <= 1){
                Pemail = 1;
            }
            else Pemail = (float) (Pemail - 0.5);

        }
        if (device1 ==1){

            if (Pdevice <= 1){
                Pdevice = 1;
            }
            else Pdevice = (float) (Pdevice - 0.5);

        }
        if (imei1 ==1){
            if (Pimei <= 1){
                Pimei = 1;
            }
            else Pimei = (float) (Pimei - 0.5);

        }

    }
                else {
        salida_calculada = 0;
        umbral = (float) (umbral - 0.5);
        if (location1 ==1){

            if (Plocation >= 9.5){
                Plocation = 10;
            }
            else Plocation = (float) (Plocation + 0.5);

        }
        if (email1 ==1){

            if (Pemail >= 9.5){
                Pemail = 10;
            }
            else Pemail = (float) (Pemail + 0.5);

        }
        if (device1 ==1){

            if (Pdevice >= 9.5){
                Pdevice = 10;
            }
            else Pdevice = (float) (Pdevice + 0.5);

        }
        if (imei1 ==1){
            if (Pimei >= 9.5){
                Pimei = 10;
            }
            else Pimei = (float) (Pimei + 0.5);

        }

    }


 */
//input umbral : umbral verde, umbral naranaja, umbral rojo
public static ArrayList<Tensor<?>> Funcionar_salida_Ant_umbral_static1(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser) {
    float n_epochs = 1;

    float output = 0; //y

    //First, create an input tensor:
    /*

     */


    Tensor input_location = Tensor.create(location);
    Tensor input_email = Tensor.create(email);
    Tensor input_imei = Tensor.create(imei);
    Tensor input_device = Tensor.create(device);
    Tensor input_serialnumber = Tensor.create(serialnumber);
    Tensor input_macaddress = Tensor.create(macaddress);
    Tensor input_advertiser = Tensor.create(advertiser);

    Tensor input_umbral_verde = Tensor.create(umbral_verde);
    Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
    Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

    Tensor input_Plocation = Tensor.create(Plocation);
    Tensor input_Pemail = Tensor.create(Pemail);
    Tensor input_Pdevice = Tensor.create(Pdevice);
    Tensor input_Pimei = Tensor.create(Pimei);
    Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
    Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
    Tensor input_Padvertiser = Tensor.create(Padvertiser);



    ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

    String location_filtrado = "NO";
    String email_filtrado = "NO";
    String imei_filtrado = "NO";
    String device_filtrado = "NO";
    String serialnumber_filtrado = "NO";
    String macaddress_filtrado = "NO";
    String advertiser_filtrado = "NO";

    String filtraciones_aplicacion = "";

    if (location == 1){
        location_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -Location" +  "\n";
    }
    if (email == 1){
        email_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -Email" +  "\n";
    }
    if (device == 1){
        device_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -DeviceID" +  "\n";
    }
    if (imei == 1){
        imei_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -Imei" +  "\n";

    }
    if (serialnumber == 1){
        serialnumber_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -SerialNumber" +  "\n";

    }
    if (macaddress == 1){
        macaddress_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -MacAddress" +  "\n";

    }
    if (advertiser == 1){
        advertiser_filtrado = "SI";
        filtraciones_aplicacion = filtraciones_aplicacion + " -AdvertiserID" +  "\n";

    }


    //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
    //umbral_green
    Tensor op_tensor_verde = sess.runner().feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_verde).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).fetch("output").run().get(0).expect(Float.class);
    //umbral Naranja
    Tensor op_tensor_naranja = sess.runner().feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_naranja).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).fetch("output").run().get(0).expect(Float.class);
    //umbral rojo
    Tensor op_tensor_rojo =sess.runner().feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("umbral",input_umbral_rojo).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).fetch("output").run().get(0).expect(Float.class);


    list_op_tensor.add(op_tensor_verde);
    list_op_tensor.add(op_tensor_naranja);
    list_op_tensor.add(op_tensor_rojo);

    //para escribir en la app en W y B test
    // LO COMENTO 12/02/2021
    // ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").run();
    // NO VA ESTA PRUEBA ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("W/read").fetch("b/read").fetch("y/output").run();

    //CREAR UN TEXTO PARA ESCRIBIR EL OUTPUT PREDECIDO: Out

    // Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada X=1");
    //  Y.setText("Salida: Y= "+ Float.toString(op_tensor.floatValue()) +", si entrada email,...");

    String recomendacion = "No hacer nada";
    String Nivel = " Bajo";
    int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
    if (op_tensor_verde.floatValue() == 1) {
        if(op_tensor_naranja.floatValue() == 1 ){
            if(op_tensor_rojo.floatValue() == 1 ){
                Nivel = " Alto";
                Nivel_color = 2;
            }
            else {
                Nivel = " Medio";
                Nivel_color = 1;
            }
        }
        else {
            Nivel = " Bajo";
            Nivel_color = 0;
        }
    }

    ///   test.setBackgroundResource(R.color.holo_green_light);


    // resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtración, se marcará dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
    //          "Recomendación: " + recomendacion);
//    subtitulo.setText("Nivel: " );
//    titulo.setText(Nivel );
    //     titulo.setTextColor(android.R.color.background_dark);

    //  resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado );
 //   resumen_app.setText("Filtraciones Spotify: "+ "\n" +  "\n" + filtraciones_aplicacion );

    //mirar  bien codigo:
    if ( Nivel_color == 0) {
        //   resumen_app.setBackgroundResource(R.color.verde);
        nivel_color = 0;
  //      resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
    }
    if ( Nivel_color == 1) {
        //   resumen_app.setBackgroundResource(R.color.naranja);
        nivel_color = 1;
  //      resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
    }
    if ( Nivel_color == 2) {
        //  resumen_app.setBackgroundResource(R.color.rojo);
        nivel_color = 2;
   //     resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
    }



    return list_op_tensor;
}


}



