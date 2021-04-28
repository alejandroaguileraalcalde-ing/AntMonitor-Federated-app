/*
 *  This file is part of AntMonitor <https://athinagroup.eng.uci.edu/projects/antmonitor/>.
 *  Copyright (C) 2018 Anastasia Shuba and the UCI Networking Group
 *  <https://athinagroup.eng.uci.edu>, University of California, Irvine.
 *
 *  AntMonitor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 2 of the License.
 *
 *  AntMonitor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with AntMonitor. If not, see <http://www.gnu.org/licenses/>.
 */
package edu.uci.calit2.federated.client.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.util.PreferenceTags;

//import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

//import com.google.android.gms.common.util.ArrayUtils;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Hieu Le
 */
public class AntMonitorLauncherActivity extends AppCompatActivity {

    //
    Button Federated ;

    public static Session sess;
    public static byte[] graphDef;
    public static byte[] variableAuxCheck;

    Graph graph;
    public static File file1;
    public static File file_leer;
    public static File file;   //este file es el que se manda al servidor ~~~~
    public static  File fileA;   //este file es el que se manda al servidor ~~~~
    public static  File fileB;   //este file es el que se manda al servidor ~~~~
    public static  File fileC;   //este file es el que se manda al servidor ~~~~
    public static   File file_descargado;
    public static   File textFile;
    public static   ArrayList<String> pesos = new ArrayList<>();
    public static   Tensor<String> checkpointPrefix;
    public static  String checkpointDir;
    public static   InputStream inputCheck;
    public static   float num_epoch = 0;
    public static   boolean isModelUpdated = false;
    //private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_monitor_launcher);
        SharedPreferences sp = getSharedPreferences(PreferenceTags.PREFS_TAG, Context.MODE_PRIVATE);
        boolean gettingStartedCompleted = sp.getBoolean(PreferenceTags.PREFS_GETTING_STARTED_COMPLETE, false);
        ////////////////////////////
     /*   // sesion
        InputStream inputStream;
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
        // Toast.makeText(getApplicationContext(), "Initialized" + sess.toString(), Toast.LENGTH_SHORT).show();


      */
        //////////////////////////
        if (!gettingStartedCompleted) {
            Intent intent = new Intent(this, GettingStartedActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AntMonitorMainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
