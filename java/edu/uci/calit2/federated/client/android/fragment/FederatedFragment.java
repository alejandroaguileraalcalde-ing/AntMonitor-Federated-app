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
package edu.uci.calit2.federated.client.android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.EditText;

import edu.uci.calit2.federated.R;


//import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
//import android.widget.Toolbar;

//import com.google.android.gms.common.util.ArrayUtils;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import edu.uci.calit2.federated.client.android.activity.Adaptador;
import edu.uci.calit2.federated.client.android.activity.Entidad;
import edu.uci.calit2.federated.client.android.database.PrivacyDB;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FederatedFragment extends  Fragment {

    /////

    public final static String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
    public static final String ERROR = "error";
    //
    private static final String TAG = "PrivacyLeaksReportF";

    private static final int LOADER_RECENT = 0;
    private static final int LOADER_WEEKLY = 1;
    private static final int LOADER_MONTHLY = 2;

    private SimpleCursorAdapter mAdapter;
    private FederatedFragment.OnFragmentInteractionListener mListener;

    BroadcastReceiver leaksChangedReceiver;
    private IntentFilter leaksFilter;

    public static final String INTENT_EXTRA_APP_NAME = "INTENT_APP_NAME";
    public static final String INTENT_EXTRA_PACKAGE_NAME = "INTENT_PACKAGE_NAME";


    private static final int LOADER_APP_HISTORY = 0;
    private String pkgName;

    private AppCompatDelegate mDelegate;
    private int mThemeId = 0;
    private Resources mResources;




    private String appName2;
    private String appNameTag;


    ///


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
    boolean isModelUpdated = false;
    static int modelctr = 11000;
    ArrayList<Float> y_mejoras_w = new ArrayList<Float>();
    ArrayList<String> x_mejoras_w = new  ArrayList<String>();
    ArrayList<Float> y_mejoras_b = new  ArrayList<Float>();
    ArrayList<String> x_mejoras_b = new ArrayList<String>();

    public static ArrayList<String> lista_nombres = new ArrayList<String>();
    public static ArrayList<String> lista_nombres_tags = new ArrayList<String>();
    public static ArrayList<CharSequence> lista_nombres_intent = new ArrayList<CharSequence>();

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
    public static String clasificacion_usuarios = "B"; //A (menos estrictos con los pesos), B(un nivel mÃ¡s normal), C( nivel muy alto)

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
    ArrayList<Entidad> listItems2 = new ArrayList<>();


    //lista de apps:
    private ListView lvItems;
    private Adaptador adaptador;

    PrivacyDB database = PrivacyDB.getInstance(getActivity());           // AQUI LOS DATOS:
    public static Map<String,Entidad> lista_apps_analizadas_final = new HashMap<String,Entidad>();
    public static Map<String,Entidad> lista_apps_analizadas_final2 = new HashMap<String,Entidad>();
    //public static HashSet<Entidad> lista_apps_analizadas_final = new HashSet<Entidad>();
    public static ArrayList<Entidad> list_app_test = new ArrayList<>();
    public static ArrayList<Entidad> list_app_test2 = new ArrayList<>();
    public static int Numero_apps = 0;
    public static String aux_num = ""+Numero_apps;
    public static ArrayList<String> nombres = new ArrayList<>();






    //???



    static {
        System.loadLibrary("tensorflow_inference");
    }


    //  private HelpFragment.OnFragmentInteractionListener mListener;

//comento
    /*
    public HelpFragment() {
        // Required empty public constructor
    }

     */
    //OJO ESTO LA LISTA:
   /*  BroadcastReceiver leaksChangedReceiver;
    private IntentFilter leaksFilter;

    */


    public FederatedFragment() throws IOException {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HelpFragment.
     */
    public static FederatedFragment newInstance(String param1, String param2) throws IOException {
        FederatedFragment fragment = new FederatedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);




        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelpFragment.OnFragmentInteractionListener) {
            mListener = (FederatedFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
