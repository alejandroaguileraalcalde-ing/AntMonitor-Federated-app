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

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.activity.AntMonitorMainActivity;
import edu.uci.calit2.federated.client.android.activity.Entidad;
//import edu.uci.calit2.anteater.client.android.activity.LoadingDialog;
import edu.uci.calit2.federated.client.android.activity.QuizActivity;
import edu.uci.calit2.federated.client.android.activity.TinyDB;
import edu.uci.calit2.federated.client.android.database.PrivacyDB;


import edu.uci.calit2.federated.client.android.activity.LeaksAppHistoryActivity;
import edu.uci.calit2.federated.client.android.activity.uielements.PrivacyLeaksReportCursorLoader;
import edu.uci.calit2.federated.client.android.util.PreferenceTags;

//federated imports

import android.content.res.Resources;
import android.os.BatteryManager;
//import android.widget.SimpleCursorAdapter;
import android.widget.EditText;


import android.os.Environment;

//import androidx.appcompat.app.AppCompatActivity;
//import android.widget.Toolbar;

//import com.google.android.gms.common.util.ArrayUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import java.util.Calendar;
import java.util.Random;
import java.util.Set;


import edu.uci.calit2.federated.client.android.activity.Adaptador;
import edu.uci.calit2.federated.client.android.activity.MainActivity;
import edu.uci.calit2.federated.client.android.activity.MasInformacion;
import edu.uci.calit2.federated.client.android.activity.MyAsyncTask;
import edu.uci.calit2.antmonitor.lib.vpn.ForwarderManager;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivacyLeaksReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivacyLeaksReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyLeaksReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
   public static boolean boton_cuestionario_final = true;
 public static String dirIP= "test"; //en la realidad aqui dejar vacío y se sustituye por la dir IP de cada App.
public static int numerodeReports = 0;
    private static final String TAG = "PrivacyLeaksReportF";
    public static Map<String,String> mapa_categorias = new HashMap<>();
public static ArrayList<String> lista_tipos_servidores = new ArrayList<>();
    private static final int LOADER_RECENT = 0;
    private static final int LOADER_WEEKLY = 1;
    private static final int LOADER_MONTHLY = 2;
    private static final String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
    private static final String URL_ESPAÑOL = "&gl=ES";

    public static final String STRING_ADVERTISING_DST = "Advertising Server";
    public static final String STRING_INTERNAL_DST = "Internal Server";
    public static final String STRING_ANALYTICS_DST = "Analytics Server";
    public static final String STRING_DEVELOPMENT_DST = "Development Server";
    public static final String STRING_SOCIAL_DST = "Social Media Networks Server";
public static int Uncomfortable =0;
    public static  int Middle = 0;
    public static  int Comfortable=0;

    //porcentaje de aciertos:
    public static int numeroAppsAnalizasTotal =0;
    public static int numeroAppsDisagree =0;


 public static String url_categoria;
 public static ArrayList<String> lista_remoteIP_test = new ArrayList<>();
    private SimpleCursorAdapter mAdapter;
    private SimpleCursorAdapter mAdapter2;
    private OnFragmentInteractionListener mListener;
 public static String nombre_dir_ip_temporal;
    BroadcastReceiver leaksChangedReceiver;
    private IntentFilter leaksFilter;
    public static ArrayList<Integer> array_quizz = new ArrayList<>();
    public static Map<String,String> mapa_nombres = new HashMap<>();
    public static Map<String,String> mapa_nombrestags = new HashMap<>();
    public static Map<String, Drawable> mapa_urlImage = new HashMap<String, Drawable>();

    public static ArrayList<String> lista_mapa_nombres = new ArrayList<String>();
    public static ArrayList<String> lista_mapa_nombrestags = new ArrayList<String>();
    //vacias
    public static Map<String,String> mapa_nombres_vacia = new HashMap<>();
    public static Map<String,String> mapa_nombrestags_vacia = new HashMap<>();
    public static Map<String, Drawable> mapa_urlImage_vacia = new HashMap<String, Drawable>();

    public static Map<String,String> mapa_url_compañias = new HashMap<>();
    public static ArrayList<String> lista_url_compañias = new ArrayList<String>();

    public static ArrayList<String> lista_mapa_nombres_vacia = new ArrayList<String>();
    public static ArrayList<Float> lista_float_vacia = new ArrayList<Float>();
    public static ArrayList<String> lista_mapa_nombrestags_vacia = new ArrayList<String>();
    public static ArrayList<Float> recogidaDatos = new ArrayList<>();

    ///////07-03
    public static Entidad Entidad0 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 1,0,0,0,0,"");
    public static Entidad Entidad1 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,1,0,0,"");
    public static Entidad Entidad2 =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,1,0,"");
    public static Entidad Entidad3 =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,1,0,0,0,"");
    public static Entidad Entidad4 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,1,"");
    public static Entidad Entidad5 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
    public static Entidad Entidad6 =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
    public static Entidad Entidad7 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
    public static Entidad Entidad8 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
    public static Entidad Entidad9 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
    public static Entidad Entidad10 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");


    //



public static boolean upload_bool;
    public static int dia_semana;
public static boolean download_bool;
    private String appName2;
    private String appNameTag;
    private String pkgName;
    private AppCompatDelegate mDelegate;
    Button button1 ;
    Button button_automatico;

    Button button2 ;
    Button button3 ;
    Button button4 ;
    Button button5 ;
    Button button6 ;
    Button button7 ;
    Button button8 ;
    Button button9 ;
    Button button10 ;
    public static Button buton_guardar;
    public static ArrayList<String> nombres_aplicaciones_mapa = new ArrayList<>();
    public static int numerobotones;
    public static int numerobotones2;

    public static boolean unaVez = true;
    public static boolean unaVez2 = true;

    public static String aux0;
    public static String aux1;
    public static String aux2;
    public static String aux3;
    public static String aux4;
    public static String aux5;
    public static String aux6;
    public static String aux7;
    public static String aux8;
    public static String aux9;
    public static String aux10;
   public static ArrayList<String> lista_recomendaciones = new ArrayList<String>();

    public static String[] splited0 = new String[0];
    public static int longitud0;
    public static String[] splited1 = new String[0];
    public static int longitud1;
    public static String[] splited2 = new String[0];
    public static int longitud2;
    public static String[] splited3 = new String[0];
    public static int longitud3;
    public static String[] splited4 = new String[0];
    public static int longitud4;
    public static String[] splited5 = new String[0];
    public static int longitud5;
    public static String[] splited6 = new String[0];
    public static int longitud6;
    public static String[] splited7 = new String[0];
    public static int longitud7;
    public static String[] splited8 = new String[0];
    public static int longitud8;
    public static String[] splited9 = new String[0];
    public static int longitud9;

    public static String categoria0 = "";
    public static String categoria1 = "";
    public static String categoria2 = "";
    public static String categoria3 = "";
    public static  String categoria4 = "";
    public static String categoria5 = "";
    public static String categoria6 = "";
    public static  String categoria7 = "";
    public static  String categoria8 = "";
    public static  String categoria9 = "";

    public static  String recomendacion = "error";
    public static boolean error = false;


    //federated
    private int mThemeId = 0;
    private Resources mResources;
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
    File fileD; // este file es el que se manda el servidor
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
    public static ArrayList<String> claves = new ArrayList<String>();
    public static ArrayList<String> claves_vacio = new ArrayList<String>();
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
   public static  ArrayList<Float> lista_location = new ArrayList<Float>();
    public static   ArrayList<Float> lista_email = new ArrayList<Float>();
    public static   ArrayList<Float> lista_device = new ArrayList<Float>();
    public static    ArrayList<Float> lista_imei = new ArrayList<Float>();
    public static    ArrayList<Float> lista_serialnumber = new ArrayList<Float>();
    public static  ArrayList<String> lista_url_button = new ArrayList<String>(); //

    public static    ArrayList<Float> lista_macaddress = new ArrayList<Float>();
    public static    ArrayList<Float> lista_advertiser = new ArrayList<Float>();
   public static  ArrayList<String> lista_nombres1 = new ArrayList<String>();
    public static    ArrayList<String> lista_ip = new ArrayList<>();
    public static    ArrayList<Float> lista_internal_dst = new ArrayList<Float>();
    public static    ArrayList<Float> lista_ads_dst = new ArrayList<Float>();
    public static    ArrayList<Float> lista_analytics_dst = new ArrayList<Float>();
    public static    ArrayList<Float> lista_develop_dst = new ArrayList<Float>();
    public static    ArrayList<Float> lista_sns_dst = new ArrayList<Float>();


    TextView B;
    TextView W;
    TextView Wtest;
    TextView Btest;
    TextView Y;
    ListView reportsListView;
    public static Button upload;
    public static Button getDirIP;

    //TextView cargando;
    ProgressBar cargando;
    ImageView foto_prueba;

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
    public static    Button button_url;
    public static   Button mostrar_resumen_final;
    public static   TextView Apps_analizadas;

    public static   float umbral = 100;
    public static float umbral_verde = 3;
    public static float umbral_naranja = 10;
    public static float umbral_rojo = 15;

    // pesos a cambiar:
    public static  float Plocation = 90;
    public static float Pemail = 90;
    public static  float Pimei = 70;
    public static  float Pdevice = 70;
    public static  float Pserialnumber = 50;
    public static  float Pmacaddress = 60;
    public static  float Padvertiser = 60;
    public static  float Pinternal_dst =  1;
    public static  float Pads_dst =  8;
    public static  float Psns_dst = 9;
    public static  float Pdevelop_dst =  1;
    public static  float Panalytics_dst =  3;
    boolean actualizacion_datos_activity = false;
    public static boolean quiz_inicio_realizado = true;
    Button info;
    Button quiz;
    TextView texto_inicio_quiz;
    TextView  texto_cuestionario_inicio_aplicacion;
    Button cuestionario_inicial_aplicacion;
    Button cuestionario_final_aplicacion;
    TextView texto_apps_analizadas;
    public static  int nivel_color = 0;
    //A Conservaties
    // B Unconcerned
    // C en el Medio
    // D Advanced Users
    public static String clasificacion_usuarios = "C";

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
    ArrayList<Entidad> listItems3 = new ArrayList<>();
    ArrayList<Entidad> listItems4 = new ArrayList<>();
    ArrayList<Entidad> listItems5 = new ArrayList<>();

    ArrayList<Entidad> listItems_vacio = new ArrayList<>();
    //int num_apps = listItems2.size();


    //lista de apps:
    private ListView lvItems;
    private ListView lvItems2;
    private ListView lvItemsvacio;
    private Adaptador adaptador;
    private Adaptador adaptador2;

    PrivacyDB database = PrivacyDB.getInstance(getActivity());           // AQUI LOS DATOS:
    public static Map<String,Entidad> lista_apps_analizadas_final = new HashMap<String,Entidad>();
    public static Map<String,Entidad> lista_apps_analizadas_final2 = new HashMap<String,Entidad>();
    public static Map<String,Entidad> lista_apps_analizadas_final3 = new HashMap<String,Entidad>();
    public static Map<String,Entidad> lista_apps_analizadas_final4 = new HashMap<String,Entidad>();
    public static ArrayList<Entidad> list_app_test = new ArrayList<>();
    public static ArrayList<Entidad> list_app_test2 = new ArrayList<>();
    public static int Numero_apps = 0;
    public static String aux_num = ""+Numero_apps;
    public static ArrayList<String> nombres = new ArrayList<>();

    public static Map<String,String> mapa_ip_test = new HashMap<>();
    public static Map<String,String> mapa_dns_test = new HashMap<>();
    public static Map<String,String> mapa_tipos_servidores = new HashMap<String,String>();
    public static Map<String,String>   Mapa_DNS_cache = new HashMap<>();
    public static Map<String,String> mapa_appname_y_dns_cache = new HashMap<>();


    static {
        System.loadLibrary("tensorflow_inference");
    }

    //


    public PrivacyLeaksReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PrivacyLeaksReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivacyLeaksReportFragment newInstance(String param1, String param2) {
        PrivacyLeaksReportFragment fragment = new PrivacyLeaksReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_privacy_reports, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_option_clear_reports:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.clear_privacy_reports_confirm_alert)
                        .setTitle(R.string.are_you_sure);
                builder.setPositiveButton(R.string.ok, new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PrivacyDB database = PrivacyDB.getInstance(getActivity());
                        database.clearLeaksHistory();
                        database.close();
                        dialog.dismiss();
                        refreshView();
                        clearPrivacyReportDialogDone();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearPrivacyReportDialogDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.clear_privacy_reports_alert)
                .setTitle(R.string.completed);
        builder.setPositiveButton(R.string.ok, new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final LoaderManager.LoaderCallbacks callbacks = this;
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_privacy_leaks_report, container, false);

        reportsListView = (ListView) view.findViewById(R.id.privacy_reports_list);
        foto_prueba = (ImageView) view.findViewById(R.id.foto_prueba);
          Apps_analizadas = (TextView) view.findViewById(R.id.Apps_analizadas);
       View listHeaderView = inflater.inflate(R.layout.fragment_privacy_leaks_report_header, null);
      reportsListView.addHeaderView(listHeaderView, null, false);
        // the columns refer to the data that will be coming from the cursor and how it ties to the layout elements
        String[] fromColumns = {PrivacyDB.COLUMN_APP, PrivacyLeaksReportCursorLoader.COLUMN_COUNT, PrivacyDB.COLUMN_APP};
        int[] toViews = {R.id.app_name, R.id.leaks_count, R.id.app_icon}; // The TextViews in list_item_privacy_reports

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_privacy_reports, null,
                fromColumns, toViews, 0);

        // Used to post-process the data for a more user-friendly display
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View toView, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(PrivacyDB.COLUMN_APP)) {
                    String pkgName = cursor.getString(columnIndex);
                    final PackageManager pm = getContext().getPackageManager();
                    ApplicationInfo appInfo = null;
                    try {
                        appInfo = pm.getApplicationInfo(pkgName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.w(TAG, "Application not found for package: " + pkgName);
                    }

                    // Even if app was not found, we need to set something,
                    // otherwise old values are re-used and inconsistencies arise
                    // Must be a peculiarity of CursorAdapter
                    if (toView instanceof ImageView) {
                        // Set app icon
                        ImageView appIcon = (ImageView) toView;
                        appIcon.setImageDrawable(appInfo == null ? null : appInfo.loadIcon(pm));

                        return true;
                    } else if (toView instanceof TextView) {
                        // Set app name instead of package name if available
                        TextView textView = (TextView) toView;
                        textView.setText(appInfo == null ? pkgName : pm.getApplicationLabel(appInfo));


                        // Keep package name as a tag
                        //Log.w(TAG, "Setting tag for " + pkgName);
                        textView.setTag(pkgName);
                        String textoVista = (String) textView.getText();
                        try{
                            mapa_nombres.put(textoVista,textoVista);
                            mapa_nombrestags.put(textoVista,pkgName);
                            mapa_urlImage.put(textoVista,appInfo.loadIcon(pm));
                            numerobotones2 = mapa_nombres.size();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        reportsListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_RECENT, null, callbacks);


        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {                               /// si se click se va a una vista con la location, imei, etc de cada app.
                TextView appNameTextView = (TextView) view.findViewById(R.id.app_name);

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 // leaksAppHistoryActiviy.class
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        (String) appNameTextView.getTag());                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appNameTextView.getText());                                                         //manda el nombre de la .com.app
               //
                CharSequence test = appNameTextView.getText(); //"keyboard for Xiaomi"
                String test2 =  (String) appNameTextView.getTag();    // "com.facemoji.lite.xiaomi
                FederatedFragment.lista_nombres_intent.add( appNameTextView.getText());
                startActivity(intent);
                
            }
        });



        RadioGroup reportGroup = (RadioGroup) view.findViewById(R.id.report_group);
     reportGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                final int NO_SELECTION = -1;
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                    view.setChecked(view.getId() == i);
                }

                if (i != NO_SELECTION && i != radioGroup.getId()) {

                    ToggleButton checkedButton = (ToggleButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (checkedButton != null) {
                        // reset loader
                        resetLoaderBasedOnRadioButtonId(checkedButton.getId());

                        // update the shared preferences
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PreferenceTags.PREFS_TAG, Context.MODE_PRIVATE);
                        sharedPreferences.edit().putInt(PreferenceTags.PREFS_LEAK_REPORT_SORT, checkedButton.getId()).apply();
                    }
                }
            }
        });



        // init the sort of the list based on user preferences
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceTags.PREFS_TAG, Context.MODE_PRIVATE);
        int sortType = sp.getInt(PreferenceTags.PREFS_LEAK_REPORT_SORT, R.id.most_recent);
        //Log.d(TAG, "sort type passed in : " + sortType);
        reportGroup.check(sortType);

        leaksChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // reset the view when there is new data
                if (intent.getAction().equals(PrivacyDB.DB_LEAK_CHANGED)) {
                    refreshView();
                }
            }
        };

        leaksFilter = new IntentFilter(PrivacyDB.DB_LEAK_CHANGED);

        //



        final Button primera_vez_servidor = view.findViewById(R.id.primera_vez_servidor);
        primera_vez_servidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(getActivity(), file, dirIP,"","ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {

                        Log.i("Output: isModelUpdated", "Done");

                    }
                });
                isGlobalModelUpdated.execute();

            }
        });
        //buton guardar:
        buton_guardar = (Button) view.findViewById(R.id.buton_guardar);

        buton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               GuardarShared();

            }
        });





        //automatico
        button_automatico = (Button) view.findViewById(R.id.button_automatico);

        button_automatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargando.setVisibility(View.VISIBLE);
                Handler handler10 = new Handler();
                handler10.postDelayed(new Runnable() {
                    public void run() {

                        reportsListView.setClickable(false);
                       button1.callOnClick();

                    }
                }, 3000); //ajustar bien tiempo

            }
        });

        //button LOAD
        button1 = (Button) view.findViewById(R.id.button_test);

        button1.setOnClickListener(new View.OnClickListener() {                  //button 'Load Report':
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onClick(View v) {
                primera_vez_servidor.callOnClick();
                Map<String, Drawable> mapa_aux = mapa_urlImage;
                int y = 0;
                int u = 0;

                for (String nombre : mapa_nombres.values()) {
                    if (lista_mapa_nombres.size() < mapa_nombres.size()) {
                        lista_mapa_nombres.add(y, nombre);
                        y++;
                    }
                }
                for (String nombre : mapa_nombrestags.values()) {
                    if (lista_mapa_nombrestags.size() < mapa_nombrestags.size()) {
                        lista_mapa_nombrestags.add(u, nombre);
                        u++;
                    }
                }
                ///si no hay apps analizadas
                if (lista_mapa_nombrestags.size() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "No apps analyzed, please try it later.", Toast.LENGTH_SHORT).show();
                    cargando.setVisibility(View.GONE);
                } else {
                    cargando.setVisibility(View.VISIBLE);
                    reportsListView.setVisibility(View.GONE);

                    mostrar_resumen_final.setClickable(true);

                    AntMonitorMainActivity.antMonitorSwitch.setChecked(false);


                    y = 0;
                    u = 0;
                    PrivacyLeaksReportFragment.list_app_test.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0, 0, 0, 0, 0, 0, 0, "  Spotify", "", 0, false, false, false, null, "", 0, 0, 0, 0, 0, ""));
                    numerobotones = mapa_nombres.size();
                    Toast.makeText(getActivity().getApplicationContext(), "analyzing: " + numerobotones + " applications...It may take some time", Toast.LENGTH_SHORT).show();

                    PrivacyDB p = new PrivacyDB(getActivity());

                    String result = "";

                    for (int i = 0; i < PrivacyLeaksReportFragment.mapa_nombres.size(); i++) {
                        Cursor c = p.getPrivacyLeaksAppHistory(PrivacyLeaksReportFragment.lista_mapa_nombrestags.get(i));
                        //   String result = "";

                        int iCOLUM_ID = c.getColumnIndex(PrivacyDB.COLUMN_ID);
                        int iCOLUMN_APP = c.getColumnIndex(PrivacyDB.COLUMN_APP);
                        int iCOLUMN_PII_LABEL = c.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL);
                        if (i == 0) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad0.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(0));

                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setImei(1);

                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setDevice(1);

                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setEmail(1);

                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setAdvertiser(1);

                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setSerialnumber(1);

                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setMacaddresss(1);

                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);

                                }
                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);

                                }

                            }
                        }
                        //entidad1
                        if (i == 1) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad1.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(1));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setImei(1);

                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setDevice(1);

                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setEmail(1);

                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setAdvertiser(1);

                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setSerialnumber(1);

                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setMacaddresss(1);

                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad1.setLocation(1);

                                }

                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);

                                }
                                //

                            }
                        }
                        //entidad3
                        if (i == 2) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad2.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(2));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad2.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad4
                        if (i == 3) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad3.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(3));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad5
                        if (i == 4) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad4.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(4));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad4.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad6
                        if (i == 5) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad5.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(5));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad5.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                //

                            }
                        }
                        //entidad7
                        if (i == 6) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad6.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(6));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad6.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad8
                        if (i == 7) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad7.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(7));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad7.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad9
                        if (i == 8) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad8.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(8));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad8.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                //

                            }
                        }
                        //entidad10
                        if (i == 9) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                //  result = result + c.getString(iCOLUM_ID) + " " + c.getString(iCOLUMN_APP) + " " + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                result = result + c.getString(iCOLUMN_PII_LABEL) + "\n";
                                PrivacyLeaksReportFragment.Entidad9.setNombre_app(PrivacyLeaksReportFragment.lista_mapa_nombres.get(9));
                                if ("IMEI".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setImei(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Device ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setDevice(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Email".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setEmail(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                if ("Advertiser ID".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setAdvertiser(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Serial Number".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setSerialnumber(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("MAC Address".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setMacaddresss(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("Location".charAt(0) == c.getString(iCOLUMN_PII_LABEL).charAt(0)) {

                                    PrivacyLeaksReportFragment.Entidad9.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }
                                if ("DEFAULT_PII_VALUE__LOCATION".charAt(2) == c.getString(iCOLUMN_PII_LABEL).charAt(2)) {

                                    PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                                    //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                                }

                                //

                            }
                        }


                        result = result + "cambio de app" + "\n" + "\n";
                        Log.i("valor : ", result);
                        c.close();
                    }



                    Handler handler10 = new Handler();
                    handler10.postDelayed(new Runnable() {
                        public void run() {

                            mostrar_resumen_final.callOnClick();

                        }
                    }, 2000);

                }
            }

                });

        button2 = (Button) view.findViewById(R.id.button_test2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion2();

            }

        });

        button3 = (Button) view.findViewById(R.id.button_test3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Aplicacion3();

            }

        });

        button4 = (Button) view.findViewById(R.id.button_test4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Aplicacion4();

            }

        });

        button5 = (Button) view.findViewById(R.id.button_test5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(4);

            }

        });

        button6 = (Button) view.findViewById(R.id.button_test6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(5);

            }

        });

        button7 = (Button) view.findViewById(R.id.button_test7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(6);

            }

        });

        button8 = (Button) view.findViewById(R.id.button_test8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(7);

            }

        });

        button9 = (Button) view.findViewById(R.id.button_test9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(8);

            }

        });
        button10 = (Button) view.findViewById(R.id.button_test10);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aplicacion(9);

            }

        });
        cargando = (ProgressBar) view.findViewById(R.id.cargando);
   // FEDERATED
        //// CATEGORIAS Y RECOMENDACIONES
        mapa_categorias.put("Overall", "apptest");  //añadir una app por categoría del PlayStore
        mapa_categorias.put("Design", "Canva");
        mapa_categorias.put("Vehicles", "Coches.net");
        mapa_categorias.put("Reference", "apptest");
        mapa_categorias.put("Comics", "Webtoon");
        mapa_categorias.put("Communication", "Telegram");
        mapa_categorias.put("Dating", "Meetic");
        mapa_categorias.put("Education", "Kahoot");
        mapa_categorias.put("Entertainment", "Netfilx");
        mapa_categorias.put("Events", "Premura");
        mapa_categorias.put("Finance", "PayPal");
        mapa_categorias.put("Drink", "Cookpad Inc");
        mapa_categorias.put("Fitness", "MyFitnessPal");
        mapa_categorias.put("Home", "Zara Home");
        mapa_categorias.put("Lifestyle", "Pinterest");
        mapa_categorias.put("Navigation", "Waze");
        mapa_categorias.put("Medical", "Curia");
        mapa_categorias.put("Audio", "SoundCloud");
        mapa_categorias.put("Magazines", "Pocket Casts");
        mapa_categorias.put("Parenting", "ImaginKids");
        mapa_categorias.put("Personalization", "Microsoft Launcher");
        mapa_categorias.put("Photography", "Snapseed");
        mapa_categorias.put("Productivity", "CamScanner");
        mapa_categorias.put("Shopping", "Amazon");
        mapa_categorias.put("Social", "Twitter");
        mapa_categorias.put("Sports", "ESPN");
        mapa_categorias.put("Tools", "Wifi Monitor");
       // mapa_categorias.put("Local", "apptest");
        mapa_categorias.put("Editors", "PicsArt");
        mapa_categorias.put("Weather", "Time & Radar");
        mapa_categorias.put("Demo", "Brawl Stars");
        mapa_categorias.put("Arcade", "Subwary Surfers");
        mapa_categorias.put("Puzzle", "Tiny Room Stories");
        mapa_categorias.put("Cards", "Solitarie");
        mapa_categorias.put("Casual", "Candy Crush");
        mapa_categorias.put("Racing", "Asphalt");
        mapa_categorias.put("Games", "apptest");
        mapa_categorias.put("Action", "Call of Duty Mobile");
        mapa_categorias.put("Adventure", "Sonic Forces");
        mapa_categorias.put("Board", "Risk");
        mapa_categorias.put("Casino", "Slotmania");
      //  mapa_categorias.put("Deportes", "");  //cuidado si juego o app de deporte
        mapa_categorias.put("Educational", "Quick Brain");
        mapa_categorias.put("Playing", "Clash Royale");
        mapa_categorias.put("Simulation", "Property Brothers Home Design");
        mapa_categorias.put("Strategy", "Kingdom Rush");
        mapa_categorias.put("Trivia", "Logo Test");
        mapa_categorias.put("Wear", "GPS Strava");
        mapa_categorias.put("Ages", "Pokemon GO");
        mapa_categorias.put("Under", "Antistress");
        mapa_categorias.put("6-8", "Unblock");
        mapa_categorias.put("Up", "My Little Pony");
        mapa_categorias.put("Create", "Microsoft Lens");
        mapa_categorias.put("Video", "Pinkfong");
        mapa_categorias.put("Play", "Domination");

        //////////////////////////////////////////////////
        ////////////////TEST:

        file1 = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
        InputStream inputStream;


        try {

            inputStream = getActivity().getAssets().open("graph_Ant_v17.pb");
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

        Graph graph = new Graph();
        sess = new Session(graph);
        //load the graph from the graphdef
        graph.importGraphDef(graphDef);
        sess.runner().addTarget("init").run();


        W = (TextView) view.findViewById(R.id.Text);
        Y = (TextView) view.findViewById(R.id.Y);
        titulo = (TextView) view.findViewById(R.id.titulo);
        subtitulo = (TextView) view.findViewById(R.id.subtitulo);
        bien = (Button) view.findViewById(R.id.bien);
        mal = (Button) view.findViewById(R.id.mal);

        button_url = (Button) view.findViewById(R.id.button_url);
        info = (Button) view.findViewById(R.id.info);
        quiz = (Button) view.findViewById(R.id.quiz);
        cuestionario_final_aplicacion = (Button) view.findViewById(R.id.cuestionario_final_aplicacion);
        cuestionario_inicial_aplicacion = (Button) view.findViewById(R.id.cuestionario_inicial_aplicacion);
        texto_inicio_quiz = (TextView) view.findViewById(R.id.texto_inicio);
        texto_cuestionario_inicio_aplicacion = (TextView) view.findViewById(R.id.texto_cuestionario_inicio_aplicacion);
        texto_apps_analizadas = (TextView) view.findViewById(R.id.Apps_analizadas);

        mostrar_resumen_final = (Button) view.findViewById(R.id.mostrar_resumen_final);


        resumen_app = (TextView) view.findViewById(R.id.resumen_app);



        lvItems = (ListView) view.findViewById(R.id.lvItems);
        lvItems2 = (ListView) view.findViewById(R.id.lvItems2);
        adaptador = new Adaptador(getActivity(), GetArrayItemsNuevo(2));
        lvItems.setAdapter(adaptador);


        /// shared preferences:
        SharedPreferences preferences_datos = this.getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);

        Plocation = preferences_datos.getFloat("Plocation",90);
        Pemail = preferences_datos.getFloat("Pemail",90);
        Pdevice = preferences_datos.getFloat("Pdevice",70);
        Pimei = preferences_datos.getFloat("Pimei",70);
        Pserialnumber = preferences_datos.getFloat("Pserialnumber",50);
        Pmacaddress = preferences_datos.getFloat("Pmacaddress",60);
        Padvertiser = preferences_datos.getFloat("Padvertiser",60);
        Pinternal_dst = preferences_datos.getFloat("Pinternal_dst", 1);
        Pads_dst = preferences_datos.getFloat("Pads_dst", 8);
        Panalytics_dst = preferences_datos.getFloat("Panalytics_dst", 3);
        Psns_dst = preferences_datos.getFloat("Psns_dst", 9);
        Pdevelop_dst = preferences_datos.getFloat("Pdevelop_dst", 1);
        clasificacion_usuarios = preferences_datos.getString("clasificacion_usuarios","C");
        Uncomfortable = preferences_datos.getInt("Uncomfortable",1);
        Middle = preferences_datos.getInt("Middle",1);
        Comfortable = preferences_datos.getInt("Comfortable",1);
        umbral_verde = preferences_datos.getFloat("umbral_verde",3);
        umbral_naranja = preferences_datos.getFloat("umbral_naranja",10);
        umbral_rojo = preferences_datos.getFloat("umbral_rojo",15);
        modelctr = preferences_datos.getInt("modelctr",0);
        dia_semana = preferences_datos.getInt("dia_semana",0);
        upload_bool = preferences_datos.getBoolean("upload_bool",false);
        download_bool = preferences_datos.getBoolean("download_bool",false);
       quiz_inicio_realizado = preferences_datos.getBoolean("quiz_inicio_realizado",true); //comentar para hacer pruebas en el quizActivity.
       boton_cuestionario_final = preferences_datos.getBoolean("boton_cuestionario_final",true); //
        numerodeReports = preferences_datos.getInt("numerodeReports",0);
        numeroAppsAnalizasTotal = preferences_datos.getInt("numeroAppsAnalizasTotal",0);
        numeroAppsDisagree = preferences_datos.getInt("numeroAppsDisagree",0);

        recogidaDatos.add(0,Plocation);
        recogidaDatos.add(1,Pemail);
        recogidaDatos.add(2,Pimei);
        recogidaDatos.add(3,Pdevice);
        recogidaDatos.add(4,Pserialnumber);
        recogidaDatos.add(5,Pmacaddress);
        recogidaDatos.add(6,Padvertiser);


        Log.i("valor loc: ",""+Plocation);
        Log.i("valor em: ",""+Pemail);
        Log.i("valor imei: ",""+Pimei);
        Log.i("valor dev: ",""+Pdevice);
        Log.i("valor ser: ",""+Pserialnumber);
        Log.i("valor mac: ",""+Pmacaddress);
        Log.i("valor ad: ",""+Padvertiser);

        Log.i("valor int_dst: ",""+Pinternal_dst);
        Log.i("valor ad_dst: ",""+Pads_dst);
        Log.i("valor analytic_dst: ",""+Panalytics_dst);
        Log.i("valor sns_dst: ",""+Psns_dst);
        Log.i("valor develop_dst: ",""+Pdevelop_dst);

        quiz_inicio_realizado = true;
        Log.i("bool auto: ",""+quiz_inicio_realizado);
        quiz_inicio_realizado = true;



        Intent intent = getActivity().getIntent();
        actualizacion_datos_activity = intent.getBooleanExtra(MainActivity.ACTUALIZACION_DATOS_ACTIVITY, false);
        if ( actualizacion_datos_activity ) {

            reportsListView.setVisibility(View.VISIBLE);
            Apps_analizadas.setVisibility(View.VISIBLE);
            mapa_nombres  =  mapa_nombres_vacia;
            mapa_nombrestags = mapa_nombrestags_vacia;
            mapa_urlImage  = mapa_urlImage_vacia;

            lista_mapa_nombres = lista_mapa_nombres_vacia;
            lista_mapa_nombrestags = lista_mapa_nombrestags_vacia;
            nombres = lista_mapa_nombres_vacia;

           actualizacion_datos_activity  =false;
            reportsListView.setVisibility(View.VISIBLE);
            Y.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            mostrar_resumen_final.setVisibility(View.VISIBLE);


            button1.setVisibility(View.VISIBLE);
            quiz_inicio_realizado= true;



        }
        GuardarShared();

        //BOTON UPLOAD
        final Button upload = view.findViewById(R.id.uploadWeights);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                writeFileAnt();
                MyAsyncTask uploadWeights = new MyAsyncTask(getActivity(), file, dirIP,"","uploadWeights", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        Log.i("Output: uploadWeights", result);




                    }
                });
                uploadWeights.execute();

                GuardarShared();


            }



        });
        //BOTON DOWN
        final Button getModel = view.findViewById(R.id.getModel);
        //upload = view.findViewById(R.id.uploadWeights);
        getModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(getActivity(), file, dirIP,"","ismodelUpdated", new MyAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                //If True, get Global Model
                MyAsyncTask getGlobalModel = new MyAsyncTask(getActivity(), file,dirIP, "","getModel", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        Log.i("Output: GetGlobalModel", result);
                    }
                });
                getGlobalModel.execute();
            //    Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                Log.i("Output: isModelUpdated", "Done");

            }
        });
        isGlobalModelUpdated.execute();
        file_descargado = new File(getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "graph_pesos");

        isModelUpdated = true;
        GuardarShared();
        }
        });



        //BOTON TEST GET DIRECCION IP
        final Button getDirIP = view.findViewById(R.id.getDirIp);
        //upload = view.findViewById(R.id.uploadWeights);
        getDirIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAsyncTask getServidorDestinoTest = new MyAsyncTask(getActivity(), file, dirIP,nombre_dir_ip_temporal,"getServidorDestinoTest", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        //If True, get Global Model


                      //  Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                        Log.i("Output: isModelUpdated", "Done");

                    }
                });
                getServidorDestinoTest.execute();


                GuardarShared();
            }
        });

////////////////
        if (Uncomfortable >=4) {
            //grupo A
            clasificacion_usuarios = "A";
        }
        if ((Uncomfortable >=2)&&(Uncomfortable <4)) {
            //grupo A
            clasificacion_usuarios = "D";
        }
        if ((Uncomfortable <2)&&(Comfortable >=2)) {
            //grupo A
            clasificacion_usuarios = "B";
        }
        else {
            clasificacion_usuarios = "C";
        }


        if (RealTimeFragment.Uncomfortable >=4) {
            //grupo A
            clasificacion_usuarios = "A";
        }
        if ((RealTimeFragment.Uncomfortable >=2)&&(RealTimeFragment.Uncomfortable <4)) {
            //grupo A
            clasificacion_usuarios = "D";
        }
        if ((RealTimeFragment.Uncomfortable <2)&&(RealTimeFragment.Comfortable >=2)) {
            //grupo A
            clasificacion_usuarios = "B";
        }
        else {
            clasificacion_usuarios = "C";
        }
        GuardarShared();


        ///////////
        Calendar rightNow2 = Calendar.getInstance();
        int currentHourIn24Format2= rightNow2.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

        int currentHourIn12Format2 = rightNow2.get(Calendar.HOUR); // return the hour in 12 hrs format (ranging from 0-11)
        Calendar c2 = Calendar.getInstance();
        int min = rightNow2.get(Calendar.MINUTE);

        int dayOfWeek = c2.get(Calendar.DAY_OF_WEEK);
        //lunes 2, martes 3, miercoles 4, jueves 5, viernes 6, sabado 7, domingo 1

        //activar servidor:
        primera_vez_servidor.callOnClick();
    // if((numerodeReports >1)&&(dayOfWeek<4)&&(dayOfWeek!=dia_semana)) {
    //test 
        if((numerodeReports >1)) {
        //para test
        clasificacion_usuarios = "A";
        //fin de test
           dia_semana = dayOfWeek;
           Handler handler12 = new Handler();
           handler12.postDelayed(new Runnable() {
               public void run() {
                   // yourMethod();

                   upload.callOnClick();


                   upload_bool = false;
                   Log.i(TAG, "9999999999999999999999999999          UPLOADED 7777!!");
                   GuardarShared();
                   //  cargando.setVisibility(View.GONE);

               }
           }, 5000); //ajustar bien tiempo
      }
       if((numerodeReports >1)&&(dayOfWeek>=4)&&(dayOfWeek!=dia_semana)) {
     //       if((numerodeReports >1)&&(dayOfWeek<7)) {
            dia_semana = dayOfWeek;
            Handler handler12 = new Handler();
            handler12.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    getModel.callOnClick();

                    download_bool=false;
                    Log.i(TAG, "9999999999999999999999999999          DOWNLOADED!!");
                    GuardarShared();
                    //  cargando.setVisibility(View.GONE);

                }
            }, 5000); //ajustar bien tiempo
        }

        if(upload_bool){
     primera_vez_servidor.callOnClick();     //para activar servidor:
     Handler handler10 = new Handler();
     handler10.postDelayed(new Runnable() {
         public void run() {
             // yourMethod();
             upload.callOnClick();

             upload_bool=false;
             Log.i(TAG, "9999999999999999999999999999          UPLOADED!!");

             //  cargando.setVisibility(View.GONE);

         }
     }, 15000); //ajustar bien tiempo


 }

        if(download_bool){
            primera_vez_servidor.callOnClick();
            Handler handler10 = new Handler();
            handler10.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    getModel.callOnClick();

                    download_bool=false;
                    Log.i(TAG, "9999999999999999999999999999          DOWNLOADED!!");



                }
            }, 15000); //ajustar bien tiempo


        }



        //BOTON MOSTRAR RESUMEN FINAL
        mostrar_resumen_final.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              int suma =  mapa_nombrestags.size();
                numeroAppsAnalizasTotal = numeroAppsAnalizasTotal + suma;
                numerodeReports++;
                GuardarShared();
                if (isModelUpdated) {

                    initializeGraph_check();
                    isModelUpdated = false;
                }

                cargando.setVisibility(View.VISIBLE);
                num_epoch = 1;
                ////////////////////

                /////////////
                //test 1/04 v2
                listItems2= listItems_vacio;
                listItems4= listItems_vacio;
                adaptador2 = new Adaptador(getActivity(), GetArrayItemsReal());

                lvItems2.setAdapter(adaptador2);


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        DNS_parte2();  //aqui añadir las comprobaciones de los destionos por app para poner las listas  con un dst u otro



                    }
                }, 19000); //25000

                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        show();
                        cargando.setVisibility(View.GONE);


                    }
                }, 24000); //30000


            }
        });


        //BOTON INFO
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i("valor loc: ",""+Plocation);
                Log.i("valor em: ",""+Pemail);
                Log.i("valor imei: ",""+Pimei);
                Log.i("valor dev: ",""+Pdevice);
                Log.i("valor ser: ",""+Pserialnumber);
                Log.i("valor mac: ",""+Pmacaddress);
                Log.i("valor ad: ",""+Padvertiser);

                Log.i("valor int_dst: ",""+Pinternal_dst);
                Log.i("valor ad_dst: ",""+Pads_dst);
                Log.i("valor analytic_dst: ",""+Panalytics_dst);
                Log.i("valor sns_dst: ",""+Psns_dst);
                Log.i("valor develop_dst: ",""+Pdevelop_dst);

                GuardarShared();

                Log.i("valor loc: ",""+Plocation);
                Log.i("valor em: ",""+Pemail);
                Log.i("valor imei: ",""+Pimei);
                Log.i("valor dev: ",""+Pdevice);
                Log.i("valor ser: ",""+Pserialnumber);
                Log.i("valor mac: ",""+Pmacaddress);
                Log.i("valor ad: ",""+Padvertiser);

                Log.i("valor int_dst: ",""+Pinternal_dst);
                Log.i("valor ad_dst: ",""+Pads_dst);
                Log.i("valor analytic_dst: ",""+Panalytics_dst);
                Log.i("valor sns_dst: ",""+Psns_dst);
                Log.i("valor develop_dst: ",""+Pdevelop_dst);

                Toast.makeText(getActivity().getApplicationContext(), "Saved Correctly", Toast.LENGTH_SHORT).show();

            }
        });


        quiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
   Intent intent = new Intent(getActivity(), QuizActivity.class);

                quiz_inicio_realizado = true;
                GuardarShared();
                   nombres = lista_mapa_nombres_vacia;

                info.setClickable(true);
                button1.setClickable(true);
                mostrar_resumen_final.setClickable(true);


                //
                startActivity(intent);
                quiz.setVisibility(View.GONE);
                texto_inicio_quiz.setVisibility(View.GONE);

                texto_cuestionario_inicio_aplicacion.setVisibility(View.GONE);
                cuestionario_final_aplicacion.setVisibility(View.GONE);
                cuestionario_inicial_aplicacion.setVisibility(View.GONE);



                Y.setVisibility(View.GONE);

            }
        });

        //BOTON cuestionario inicio
        cuestionario_inicial_aplicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/AiELRhATkKyTBivL6"));

                nombres = lista_mapa_nombres_vacia;

                info.setClickable(true);
                button1.setClickable(true);
                mostrar_resumen_final.setClickable(true);


                //
                startActivity(intent);
                quiz.setVisibility(View.GONE);
                texto_inicio_quiz.setVisibility(View.GONE);

                texto_cuestionario_inicio_aplicacion.setVisibility(View.GONE);
                cuestionario_final_aplicacion.setVisibility(View.GONE);
                cuestionario_inicial_aplicacion.setVisibility(View.GONE);



                Y.setVisibility(View.GONE);
                texto_apps_analizadas.setVisibility(View.VISIBLE);


            }
        });

        //BOTON cuestionario final
        cuestionario_final_aplicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/GR5a7CMhCBUSJaNN6"));

                nombres = lista_mapa_nombres_vacia;

                info.setClickable(true);
                button1.setClickable(true);
                mostrar_resumen_final.setClickable(true);


                //
                startActivity(intent);
                quiz.setVisibility(View.GONE);
                texto_inicio_quiz.setVisibility(View.GONE);

                texto_cuestionario_inicio_aplicacion.setVisibility(View.GONE);
                cuestionario_final_aplicacion.setVisibility(View.GONE);
                cuestionario_inicial_aplicacion.setVisibility(View.GONE);



                Y.setVisibility(View.GONE);
                texto_apps_analizadas.setVisibility(View.VISIBLE);

               boton_cuestionario_final = false;
               GuardarShared();

            }
        });

        //upload y download automaticos:
        //ejecutar en background thread
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();



        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

        int currentHourIn12Format = rightNow.get(Calendar.HOUR); // return the hour in 12 hrs format (ranging from 0-11)

       // mostrar_resumen_final.setClickable(false);
if (quiz_inicio_realizado){
    reportsListView.setVisibility(View.VISIBLE);
    Y.setVisibility(View.GONE);
    quiz.setVisibility(View.GONE);
    texto_inicio_quiz.setVisibility(View.GONE);

    texto_cuestionario_inicio_aplicacion.setVisibility(View.GONE);
    cuestionario_final_aplicacion.setVisibility(View.GONE);
    cuestionario_inicial_aplicacion.setVisibility(View.GONE);

    if((numerodeReports>4)&&(boton_cuestionario_final)){

    }

    info.setClickable(true);
    button1.setClickable(true);
    mostrar_resumen_final.setClickable(true);

    //A Conservaties
    // B Unconcerned
    // C en el Medio
    // D Advanced Users
    if (Uncomfortable >=4) {
        //grupo A
        clasificacion_usuarios = "A";
    }
    if ((Uncomfortable >=2)&&(Uncomfortable <4)) {
        //grupo A
        clasificacion_usuarios = "D";
    }
    if ((Uncomfortable <2)&&(Comfortable >=2)) {
        //grupo A
        clasificacion_usuarios = "B";
    }
    else {
        clasificacion_usuarios = "C";
    }


    if (RealTimeFragment.Uncomfortable >=4) {
        //grupo A
        clasificacion_usuarios = "A";
    }
    if ((RealTimeFragment.Uncomfortable >=2)&&(RealTimeFragment.Uncomfortable <4)) {
        //grupo A
        clasificacion_usuarios = "D";
    }
    if ((RealTimeFragment.Uncomfortable <2)&&(RealTimeFragment.Comfortable >=2)) {
        //grupo A
        clasificacion_usuarios = "B";
    }
    else {
        clasificacion_usuarios = "C";
    }



    Log.i("bool auto: ",""+quiz_inicio_realizado);
    GuardarShared();
if (quiz_inicio_realizado){
    button_automatico.callOnClick();  //automatizar el reporte //test 14-04, no hace falta que se mire si hacer o no el button automatico. Este comprueba si map_name size es 0 o no.
}


}


///
        return view;
    }
    public Object setTag_aux(final Object tag) {
        return tag;

    }
    public void onResume() {
        super.onResume();
        refreshView();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(leaksChangedReceiver, leaksFilter);
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(leaksChangedReceiver);


    }
    private void resetLoaderBasedOnRadioButtonId(int radioId) {
        if (radioId == R.id.most_recent) {
            getLoaderManager().restartLoader(LOADER_RECENT, null, this);
        } else if (radioId == R.id.last_week) {
            getLoaderManager().restartLoader(LOADER_WEEKLY, null, this);
        } else if (radioId == R.id.last_month) {
            getLoaderManager().restartLoader(LOADER_MONTHLY, null, this);
        }
    }

    private void refreshView() {
        SharedPreferences sp = getContext().getSharedPreferences(PreferenceTags.PREFS_TAG, Context.MODE_PRIVATE);                    //actualizacion de vista.
        int sortType = sp.getInt(PreferenceTags.PREFS_LEAK_REPORT_SORT, R.id.most_recent);
        resetLoaderBasedOnRadioButtonId(sortType);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //test 1/04 v2
        listItems2= listItems_vacio;
        listItems4= listItems_vacio;

        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_RECENT:
                return new PrivacyLeaksReportCursorLoader(getContext(), PrivacyLeaksReportCursorLoader.RECENT_SORT);
            case LOADER_WEEKLY:
                return new PrivacyLeaksReportCursorLoader(getContext(), PrivacyLeaksReportCursorLoader.WEEKLY_SORT);
            case LOADER_MONTHLY:
                return new PrivacyLeaksReportCursorLoader(getContext(), PrivacyLeaksReportCursorLoader.MONTHLY_SORT);
            default:
                return new PrivacyLeaksReportCursorLoader(getContext(), PrivacyLeaksReportCursorLoader.RECENT_SORT);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition =firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void Aplicacion2(){

        //     for (int i = 0; i < lista_mapa_nombres.size(); i++) { //solo guardo en el mapa la primera de todas ?????
        int i = 1;
        // int i = 1;
        if (i==1){
            //puede estar vacio, si eso pasa no se ha analizado ninguna app:
            try {




                //
                //   mapa_nombres.get(lista_mapa_nombres.get(i));

                appName2 = (String) lista_mapa_nombres.get(i);                                    //deberÃ¡ ser una lista o algo
                appNameTag = (String) lista_mapa_nombrestags.get(i);   // "com.facemoji.lite.xiaomi //deberÃ¡ ser una lista o algo


                /////

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 // leaksAppHistoryActiviy.class
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        appNameTag );                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appName2);                                                         //manda el nombre de la .com.app
                //


                startActivity(intent);
                int aux = i +1;

                PrivacyLeaksReportFragment.Numero_apps = 0;

                //al acabar clean las dos listas: igualarlas a una vacia string arraylist
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        ArrayList<String> lista_aux = nombres_aplicaciones_mapa;
        int u = 0;
    }
    public void Aplicacion1(){


        //     for (int i = 0; i < lista_mapa_nombres.size(); i++) { //solo guardo en el mapa la primera de todas ?????
        int i = 0;
        // int i = 1;
        if (i==0){
            //puede estar vacio, si eso pasa no se ha analizado ninguna app:
            try {




                //
                //   mapa_nombres.get(lista_mapa_nombres.get(i));
                 int y = 0;
                appName2 = (String) lista_mapa_nombres.get(i);
                appNameTag = (String) lista_mapa_nombrestags.get(i);   //


                /////

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 //
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        appNameTag );                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appName2);                                                         //manda el nombre de la .com.app
                //
      //          intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME2,
      //                  (String) lista_mapa_nombrestags.get(1));                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
      //          intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME2,
      //                  lista_mapa_nombres.get(1));

                startActivity(intent);
                int aux = i +1;

                PrivacyLeaksReportFragment.Numero_apps = 0;

                //al acabar clean las dos listas: igualarlas a una vacia string arraylist
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        ArrayList<String> lista_aux = nombres_aplicaciones_mapa;
        int u = 0;
    }
    public void Aplicacion3(){


        //     for (int i = 0; i < lista_mapa_nombres.size(); i++) { //solo guardo en el mapa la primera de todas ?????
        int i = 2;
        // int i = 1;
        if (i==2){
            //puede estar vacio, si eso pasa no se ha analizado ninguna app:
            try {




                //
                //   mapa_nombres.get(lista_mapa_nombres.get(i));
                int y = 0;
                appName2 = (String) lista_mapa_nombres.get(i);                                    //deberÃ¡ ser una lista o algo
                appNameTag = (String) lista_mapa_nombrestags.get(i);   // "com.facemoji.lite.xiaomi //deberÃ¡ ser una lista o algo


                /////

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 // leaksAppHistoryActiviy.class
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        appNameTag );                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appName2);                                                         //manda el nombre de la .com.app


                startActivity(intent);
                int aux = i +1;

                PrivacyLeaksReportFragment.Numero_apps = 0;

                //al acabar clean las dos listas: igualarlas a una vacia string arraylist
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        ArrayList<String> lista_aux = nombres_aplicaciones_mapa;
        int u = 0;
    }

    public void Aplicacion4(){


        //     for (int i = 0; i < lista_mapa_nombres.size(); i++) { //solo guardo en el mapa la primera de todas ?????
        int i = 3;
        // int i = 1;
        if (i==3){
            //puede estar vacio, si eso pasa no se ha analizado ninguna app:
            try {




                //
                //   mapa_nombres.get(lista_mapa_nombres.get(i));
                int y = 0;
                appName2 = (String) lista_mapa_nombres.get(i);                                    //deberÃ¡ ser una lista o algo
                appNameTag = (String) lista_mapa_nombrestags.get(i);   // "com.facemoji.lite.xiaomi //deberÃ¡ ser una lista o algo


                /////

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 // leaksAppHistoryActiviy.class
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        appNameTag );                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appName2);                                                         //manda el nombre de la .com.app
                //
                //          intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME2,
                //                  (String) lista_mapa_nombrestags.get(1));                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                //          intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME2,
                //                  lista_mapa_nombres.get(1));

                startActivity(intent);
                int aux = i +1;

                PrivacyLeaksReportFragment.Numero_apps = 0;

                //al acabar clean las dos listas: igualarlas a una vacia string arraylist
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        ArrayList<String> lista_aux = nombres_aplicaciones_mapa;
        int u = 0;
    }
    public void Aplicacion(int i){



            try {



                appName2 = (String) lista_mapa_nombres.get(i);                                    //deberÃ¡ ser una lista o algo
                appNameTag = (String) lista_mapa_nombrestags.get(i);   // "com.facemoji.lite.xiaomi //deberÃ¡ ser una lista o algo


                /////

                Intent intent = new Intent(getActivity(), LeaksAppHistoryActivity.class);                 // leaksAppHistoryActiviy.class
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_PACKAGE_NAME,
                        appNameTag );                                                 //manda el tag, que no el nombre de la app. keyboard xiaomi
                intent.putExtra(LeaksAppHistoryActivity.INTENT_EXTRA_APP_NAME,
                        appName2);                                                         //manda el nombre de la .com.app


                startActivity(intent);
                PrivacyLeaksReportFragment.Numero_apps = 0;

                //al acabar clean las dos listas: igualarlas a una vacia string arraylist
            } catch (Exception e) {
                e.printStackTrace();
            }



        ArrayList<String> lista_aux = nombres_aplicaciones_mapa;
        int u = 0;
    }
    // FEDERATED
    /////////////////////////
    //////////////////////////
    ///

    public  void GuardarShared() {
        SharedPreferences preferences_datos_guardar = this.getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences_datos_guardar.edit();

        Obj_editor.putFloat("umbral_verde", umbral_verde);
        Obj_editor.putFloat("umbral_naranja", umbral_naranja);
        Obj_editor.putFloat("umbral_rojo", umbral_rojo);
        Obj_editor.putInt("modelctr", modelctr);
        Obj_editor.putInt("dia_semana", dia_semana);
        Obj_editor.putBoolean("upload_bool", upload_bool);
        Obj_editor.putBoolean("download_bool", download_bool);
      //  Obj_editor.putFloat("Plocation", Plocation);



        Obj_editor.putFloat("Plocation", Plocation);
        Obj_editor.putFloat("Pemail", Pemail);
        Obj_editor.putFloat("Pimei", Pimei);
        Obj_editor.putFloat("Pdevice", Pdevice);
        Obj_editor.putFloat("Pserialnumber", Pserialnumber);
        Obj_editor.putFloat("Pmacaddress", Pmacaddress);
        Obj_editor.putFloat("Padvertiser", Padvertiser);
        Obj_editor.putString("clasificacion_usuarios", clasificacion_usuarios);

        Obj_editor.putInt("Uncomfortable", Uncomfortable);
        Obj_editor.putInt("Middle", Middle);
        Obj_editor.putInt("Comfortable", Comfortable);

        Obj_editor.putFloat("Pinternal_dst", Pinternal_dst);
        Obj_editor.putFloat("Pads_dst", Pads_dst);
        Obj_editor.putFloat("Panalytics_dst", Panalytics_dst);
        Obj_editor.putFloat("Psns_dst", Psns_dst);
        Obj_editor.putFloat("Pdevelop_dst", Pdevelop_dst);
        Obj_editor.putBoolean("quiz_inicio_realizado", quiz_inicio_realizado);
        Obj_editor.putBoolean("boton_cuestionario_final", boton_cuestionario_final);
        Obj_editor.putInt("numerodeReports", numerodeReports);

        Obj_editor.putInt("numeroAppsAnalizasTotal", numeroAppsAnalizasTotal);
        Obj_editor.putInt("numeroAppsDisagree", numeroAppsDisagree);







        Obj_editor.commit();



    }
    public void saveMap(Map<String, Entidad> inputMap){
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map1").commit();
            editor.putString("My_map1", jsonString);
            editor.commit();
        }
    }
    public void saveMapString(Map<String, String> inputMap){
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map2").commit();
            editor.putString("My_map2", jsonString);
            editor.commit();
        }
    }
    public void saveMapString2(Map<String, String> inputMap){
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map4").commit();
            editor.putString("My_map4", jsonString);
            editor.commit();
        }
    }
    public void saveMapUrl(Map<String, Drawable> inputMap){
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map3").commit();
            editor.putString("My_map3", jsonString);
            editor.commit();
        }
    }

    public Map<String,Boolean> loadMap(){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map1", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

    public Map<String,Entidad> loadMapEntidad(){
        Map<String,Entidad> outputMap = new HashMap<String,Entidad>();
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map1", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Entidad value = (Entidad) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
    public Map<String,Boolean> loadMapString(){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map2", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
    public Map<String,Boolean> loadMapUrl(){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map3", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
    public Map<String,Boolean> loadMapString2(){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        SharedPreferences pSharedPref = this.getActivity().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map4", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }




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
    public static void funcionMal_static (float location3, float email3, float device3, float imei3, float serialnumber3, float macaddress3, float advertiser3, Context context, float internal_dst3, float ads_dst3, float sns_dst3, float analytics_dst3, float develop_dst3) {


        int epochs = (int)1;

        float location1 = location3;
        float email1 =  email3;
        float device1 = device3;
        float imei1 =  imei3;
        float serialnumber1 = serialnumber3;
        float macaddress1 = macaddress3;
        float advertiser1 = advertiser3;
        float internal_dst1 = internal_dst3;
        float ads_dst1 = ads_dst3;
        float sns_dst1 = sns_dst3;
        float analytics_dst1 = analytics_dst3;
        float develop_dst1 = develop_dst3;

        Log.i("valor : ", String.valueOf(location1));
        Log.i("valor : ", String.valueOf(email1));
        Log.i("valor : ", String.valueOf(device1));
        Log.i("valor : ", String.valueOf(internal_dst1));
        Log.i("valor : ", String.valueOf(sns_dst1));
        Log.i("valor : ", String.valueOf(serialnumber1));
        Log.i("valor : ", String.valueOf(develop_dst1));
        Log.i("valor : ", String.valueOf(imei1));


        Tensor salida_calculada = Funcionar_salida_Ant_umbral_static(location1, email1, imei1, device1, serialnumber1, macaddress1, advertiser1, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);
        //   float salida_calculada_umbral_verde = salida_calculada.get(0).floatValue();
        //  float salida_calculada_umbral_naranja = salida_calculada.get(1).floatValue();
        //  float salida_calculada_umbral_rojo = salida_calculada.get(2).floatValue();

        //ajusto umbral_Verde



     boolean seguir_entrenamiento= true;

        if(nivel_color == 0) {

            train_Ant_test_static(location1, email1, device1, imei1, serialnumber1, macaddress1, advertiser1, salida_calculada.floatValue(), 1, 150, internal_dst1, ads_dst1, sns_dst1, analytics_dst1, develop_dst1);
            Tensor salida_calculada2 = Funcionar_salida_Ant_umbral_static(location1, email1, imei1, device1, serialnumber1, macaddress1, advertiser1, internal_dst1, ads_dst1, sns_dst1, analytics_dst1, develop_dst1);
            Log.i("valor antes : ", String.valueOf(salida_calculada.floatValue()));
            Log.i("valor despue: ", String.valueOf(salida_calculada2.floatValue()));
        }
        if(nivel_color == 1) {
            train_Ant_test_static(location1, email1, device1, imei1, serialnumber1, macaddress1, advertiser1, salida_calculada.floatValue(),1, umbral_naranja, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);
        }
        if(nivel_color == 2) {

            train_Ant_test_static(location1, email1, device1, imei1, serialnumber1, macaddress1, advertiser1, salida_calculada.floatValue(), 1, 50, internal_dst1, ads_dst1, sns_dst1, analytics_dst1, develop_dst1);
            Tensor salida_calculada2 = Funcionar_salida_Ant_umbral_static(location1, email1, imei1, device1, serialnumber1, macaddress1, advertiser1, internal_dst1, ads_dst1, sns_dst1, analytics_dst1, develop_dst1);
            Log.i("valor antes : ", String.valueOf(salida_calculada.floatValue()));
            Log.i("valor despues: ", String.valueOf(salida_calculada2.floatValue()));
        }

      //  Log.i("valor antes : ", String.valueOf(salida_calculada.floatValue()));
       // Log.i("valor despues entrenar : ", String.valueOf(salida_calculada2.floatValue()));
       //

        num++;


        Toast.makeText(context, "We will keep improving, sorry for the inconvenience", Toast.LENGTH_SHORT).show();


    }

    public static void  test(Context context){
        int i = 0;
        Toast.makeText(context, "Seguiremos mejorando, disculpe las molestias", Toast.LENGTH_SHORT).show();
    }
    // funcion bien:
    public static void funcionBien_static (float location3, float email3, float device3, float imei3, float serialnumber3, float macaddress3, float advertiser3, Context context, float internal_dst3, float ads_dst3, float sns_dst3, float analytics_dst3, float develop_dst3) {

        int epochs = (int) 1;

        float location1 = location3;
        float email1 =  email3;
        float device1 = device3;
        float imei1 =  imei3;
        float serialnumber1 = serialnumber3;
        float macaddress1 = macaddress3;
        float advertiser1 = advertiser3;
        float internal_dst1 = internal_dst3;
        float ads_dst1 = ads_dst3;
        float sns_dst1 = sns_dst3;
        float analytics_dst1 = analytics_dst3;
        float develop_dst1 = develop_dst3;


        Tensor salida_calculada = Funcionar_salida_Ant_umbral_static(location1, email1, imei1, device1, serialnumber1, macaddress1, advertiser1, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);

        if (nivel_color == 0) {
            train_Ant_test_static(location1, email1, device1, imei1,serialnumber1, macaddress1, advertiser1,  salida_calculada.floatValue(),1, 50, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);
        }
        if (nivel_color == 1) {
            train_Ant_test_static(location1, email1, device1, imei1,serialnumber1, macaddress1, advertiser1, salida_calculada.floatValue(), 1,umbral_naranja, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);
        }
        if (nivel_color == 2) {
            train_Ant_test_static(location1, email1, device1, imei1, serialnumber1, macaddress1, advertiser1,salida_calculada.floatValue(),1, 150, internal_dst1, ads_dst1,sns_dst1,analytics_dst1,develop_dst1);
        }
        Toast.makeText(context, "Perfect!", Toast.LENGTH_SHORT).show();
    }


    private ArrayList<Entidad> GetArrayItems(){
        //  ArrayList<Entidad> listItems = new ArrayList<>();
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,""));
        listItems.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,""));

        return listItems;
    }

    @SuppressLint("JavascriptInterface")
    private ArrayList<Entidad> GetArrayItemsReal() {





boolean terminar = false;


        int num= mapa_nombres.size();
        //proceso de ip y asiganlo a cada Entidad0, Entidad1, etc
        PrivacyDB p = new PrivacyDB(getActivity());
        String[] intento_de_ip = p.printLeaks();

        TinyDB tinydb = new TinyDB(getActivity());
        for (int i=0; i<intento_de_ip.length;i++){
            String appName = intento_de_ip[i].split("[\\s,]+")[1];
            String ip = intento_de_ip[i].split("[\\s,]+")[2];
            mapa_ip_test.put(appName,ip);
            Mapa_DNS_cache =ForwarderManager.getmDNScache();
            String DNS_cache =  Mapa_DNS_cache.get(ip);


            if(DNS_cache != null){
                //guardar share al acabar el for. Para persistencia de los dns de las apps pasadas
                mapa_appname_y_dns_cache.put(appName,DNS_cache);
            //    Log.i("Output: DNS cache:", "***"+DNS_cache + "***");
                tinydb.putString(appName, DNS_cache);
            }
            int u = 9;

        }
        // tn = new TinyDB();





        Mapa_DNS_cache =ForwarderManager.getmDNScache();

        for (int i=0; i<mapa_nombrestags.size(); i++){
          cargando.setVisibility(View.VISIBLE);
          String DNS_cache =  Mapa_DNS_cache.get(mapa_ip_test.get(lista_mapa_nombrestags.get(i)));
            if (DNS_cache == null) {
                DNS_cache = tinydb.getString(lista_mapa_nombrestags.get(i));
            }
            if(DNS_cache == null){
                DNS_cache = "error";
            }
            Log.i("Output: DNS cache:", "***"+DNS_cache + "***");
            mapa_dns_test.put(""+i,DNS_cache);

        }
        //direcciones buenas de servidor destiono por categorias:
        for (int i=0; i<mapa_nombrestags.size(); i++){

               //Aquí hay que mandar las direcciones DNS buenas, estas van donde pone dirIP, se sacan de un mapa dns_test
            Log.i("Output: DNS destino222:",  mapa_dns_test.get(""+i));
            dirIP =  mapa_dns_test.get(""+i);


            MyAsyncTask getServidorDestino = new MyAsyncTask(getActivity(), file, dirIP,lista_mapa_nombrestags.get(i),"getServidorDestino", new MyAsyncTask.AsyncResponse() {
                @Override
                public void processFinish(String result) {

                    Log.i("Output: Server dst", "Done");

                }
            });
            getServidorDestino.execute();

            Handler handler10 = new Handler();
            handler10.postDelayed(new Runnable() {
                public void run() {


                }
            }, 1000); //ajustar bien tiempo


        }





//comentado 1/04/2021

                // yourMethod();
                //cuando acabe ya tengo en el mapa de categorias_tip_ip_servidor destino.
                for (int i=0; i<mapa_nombrestags.size(); i++){
                    if(i==0) Entidad0.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(0)));
                    if(i==1) Entidad1.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(1)));
                    if(i==2) Entidad2.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(2)));
                    if(i==3) Entidad3.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(3)));
                    if(i==4) Entidad4.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(4)));
                    if(i==5) Entidad5.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(5)));
                    if(i==6) Entidad6.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(6)));
                    if(i==7) Entidad7.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(7)));
                    if(i==8) Entidad8.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(8)));
                    if(i==9) Entidad9.setRecomendacion(mapa_tipos_servidores.get(lista_mapa_nombrestags.get(9)));
                }


                ///////////////



                ArrayList<String> lista_aux = lista_remoteIP_test;
                Map<String, String> mapa_aux = mapa_ip_test;
               
                if( num== 1)  {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    listItems2.add(Entidad0);}
                if( num== 2) {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));

                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                }
                if( num== 3){
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                }
                if( num== 4){
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                }
                if( num== 5) {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                }
                if( num== 6){
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));
                    Entidad5.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(5)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                    listItems2.add(Entidad5);
                }
                if( num== 7) {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));
                    Entidad5.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(5)));
                    Entidad6.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(6)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                    listItems2.add(Entidad5);
                    listItems2.add(Entidad6);
                }
                if( num== 8){
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));
                    Entidad5.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(5)));
                    Entidad6.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(6)));
                    Entidad7.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(7)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                    listItems2.add(Entidad5);
                    listItems2.add(Entidad6);
                    listItems2.add(Entidad7);
                }
                if( num== 9) {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));
                    Entidad5.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(5)));
                    Entidad6.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(6)));
                    Entidad7.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(7)));
                    Entidad8.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(8)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                    listItems2.add(Entidad5);
                    listItems2.add(Entidad6);
                    listItems2.add(Entidad7);
                    listItems2.add(Entidad8);
                }
                if( num== 10)  {
                    Entidad0.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(0)));
                    Entidad1.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(1)));
                    Entidad2.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(2)));
                    Entidad3.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(3)));
                    Entidad4.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(4)));
                    Entidad5.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(5)));
                    Entidad6.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(6)));
                    Entidad7.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(7)));
                    Entidad8.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(8)));
                    Entidad9.setIp(mapa_ip_test.get(lista_mapa_nombrestags.get(9)));

                    listItems2.add(Entidad0);
                    listItems2.add(Entidad1);
                    listItems2.add(Entidad2);
                    listItems2.add(Entidad3);
                    listItems2.add(Entidad4);
                    listItems2.add(Entidad5);
                    listItems2.add(Entidad6);
                    listItems2.add(Entidad7);
                    listItems2.add(Entidad8);
                    listItems2.add(Entidad9);
                }

                listItems4= listItems2;


              //  return listItems4;

                //  cargando.setVisibility(View.GONE);



            return listItems4;

    }

    private ArrayList<Entidad> GetArrayItemsNuevo(int i){
        //  ArrayList<Entidad> listItems = new ArrayList<>();
        Entidad auxiliar = new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0,0,0,0, 1,1,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
        for ( i=0; i<numerobotones2; i++) {
            listItems.add(auxiliar);

        }
        return listItems;
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


        String recomendacion = "No hacer nada";
        if (op_tensor.floatValue() == 1) {
            recomendacion = "Revisar App";
        }

        resumen_app.setText("Resumen App analizada: "+ "\n"  + "Nota: Si una app tiene una filtraciÃ³n, se marcarÃ¡ dicho valor con un 'SI'"+ "\n"  + "Location: " + location_filtrado + "\n" + "Email: " + email_filtrado + "\n" + "DeviceID: " + device_filtrado + "\n" + "Imei: " + imei_filtrado + "\n" +
                "RecomendaciÃ³n: " + recomendacion);
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



        return list_op_tensor;
    }
    //// PARA LAS LISTAS:

    //input umbral : umbral verde, umbral naranaja, umbral rojo
    private String Funcionar_salida_Ant_umbral_lista_Dentro(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser, float internal_dst, float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {

        float n_epochs = 1;

       //y
        String op_string;


        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);
        Tensor input_internal_dst = Tensor.create(internal_dst);
        Tensor input_ads_dst = Tensor.create(ads_dst);
        Tensor input_sns_dst = Tensor.create(sns_dst);
        Tensor input_develop_dst = Tensor.create(develop_dst);
        Tensor input_analytics_dst = Tensor.create(analytics_dst);


        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);
        Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
        Tensor input_Pads_dst = Tensor.create(Pads_dst);
        Tensor input_Psns_dst = Tensor.create(Psns_dst);
        Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
        Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);


        Tensor location_assign = Tensor.create((float)9);



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

        try {
            sess.runner()
                    .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                    .addTarget("Plocation/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //email
        try {
            sess.runner()
                    .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                    .addTarget("Pemail/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imei
        try {
            sess.runner()
                    .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                    .addTarget("Pimei/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //device
        try {
            sess.runner()
                    .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevice/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pserialnumber
        try {
            sess.runner()
                    .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                    .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pmacaddress
        try {
            sess.runner()
                    .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                    .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Padvertiser
        try {
            sess.runner()
                    .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                    .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPinternal_dst
        try {
            sess.runner()
                    .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPads_dst
        try {
            sess.runner()
                    .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Panalytics_dst
        try {
            sess.runner()
                    .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Psns_dst
        try {
            sess.runner()
                    .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pdevelop_dst
        try {
            sess.runner()
                    .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        Tensor output_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).fetch("output").run().get(0).expect(Float.class);



        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo


        //colores:
        if (output_tensor.floatValue() < umbral) {
            Nivel = " Bajo";
            Nivel_color = 0;
        }
        if (output_tensor.floatValue() >= umbral) {
            Nivel = " Alto";
            Nivel_color = 2;
        }


       String url_aux = "<a href='\"+ https://stackoverflow.com/questions/15777127/string-as-a-link-in-textview +\"'>\"+ mobileUrl +\"</a>";
        op_string = "Exposed: "+ "\n" +  "\n" + filtraciones_aplicacion  ;

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
    private String Funcionar_salida_Ant_umbral_lista_Dentro_destino(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser, float internal_dst, float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {

        float n_epochs = 1;

        //y
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
        Tensor input_internal_dst = Tensor.create(internal_dst);
        Tensor input_ads_dst = Tensor.create(ads_dst);
        Tensor input_sns_dst = Tensor.create(sns_dst);
        Tensor input_develop_dst = Tensor.create(develop_dst);
        Tensor input_analytics_dst = Tensor.create(analytics_dst);


        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);
        Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
        Tensor input_Pads_dst = Tensor.create(Pads_dst);
        Tensor input_Psns_dst = Tensor.create(Psns_dst);
        Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
        Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);


        Tensor location_assign = Tensor.create((float)9);



        ArrayList<Tensor<?>> list_op_tensor = new ArrayList<Tensor<?>>();

        String location_filtrado = "NO";
        String email_filtrado = "NO";
        String imei_filtrado = "NO";
        String device_filtrado = "NO";
        String serialnumber_filtrado = "NO";
        String macaddress_filtrado = "NO";
        String advertiser_filtrado = "NO";

        String filtraciones_aplicacion = "";


        if (internal_dst == 1){

            filtraciones_aplicacion = filtraciones_aplicacion + " Data destination: Internal Server " +  "\n";

        }
        if (ads_dst == 1){

            filtraciones_aplicacion = filtraciones_aplicacion+ " Data destination: Advertiser Server " +  "\n";

        }
        if (sns_dst == 1){

            filtraciones_aplicacion = filtraciones_aplicacion + " Data destination: Social Media Networks Server " +  "\n";

        }
        if (analytics_dst == 1){

            filtraciones_aplicacion = filtraciones_aplicacion + " Data destination: Analytics Server " +  "\n";

        }
        if (develop_dst == 1){

            filtraciones_aplicacion = filtraciones_aplicacion + " Data destination: Development Server " +  "\n";

        }


        //Tensor op_tensor = sess.runner().feed("input",input).fetch("output").run().get(0).expect(Float.class);
        //umbral_green

        ///++++++++++++++++
        //INTENTO DE PERSISTENCIA EN SESSION CON ASSIGN:
        //            actualizo el valor de los datos tras entrenarse.
        //locaiton
        try {
            sess.runner()
                    .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                    .addTarget("Plocation/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //email
        try {
            sess.runner()
                    .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                    .addTarget("Pemail/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imei
        try {
            sess.runner()
                    .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                    .addTarget("Pimei/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //device
        try {
            sess.runner()
                    .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevice/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pserialnumber
        try {
            sess.runner()
                    .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                    .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pmacaddress
        try {
            sess.runner()
                    .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                    .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Padvertiser
        try {
            sess.runner()
                    .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                    .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPinternal_dst
        try {
            sess.runner()
                    .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPads_dst
        try {
            sess.runner()
                    .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Panalytics_dst
        try {
            sess.runner()
                    .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Psns_dst
        try {
            sess.runner()
                    .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pdevelop_dst
        try {
            sess.runner()
                    .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        Tensor output_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).fetch("output").run().get(0).expect(Float.class);



        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo


        //colores:
        if (output_tensor.floatValue() < umbral) {
            Nivel = " Bajo";
            Nivel_color = 0;
        }
        if (output_tensor.floatValue() >= umbral) {
            Nivel = " Alto";
            Nivel_color = 2;
        }

        //

        op_string =  filtraciones_aplicacion;

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

    private String Funcionar_salida_Ant_umbral_lista_Nivel(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser, float internal_dst, float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {
        float n_epochs = 1;

       //y
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
        Tensor input_internal_dst = Tensor.create(internal_dst);
        Tensor input_ads_dst = Tensor.create(ads_dst);
        Tensor input_sns_dst = Tensor.create(sns_dst);
        Tensor input_develop_dst = Tensor.create(develop_dst);
        Tensor input_analytics_dst = Tensor.create(analytics_dst);


        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);
        Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
        Tensor input_Pads_dst = Tensor.create(Pads_dst);
        Tensor input_Psns_dst = Tensor.create(Psns_dst);
        Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
        Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);

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

        try {
            sess.runner()
                    .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                    .addTarget("Plocation/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //email
        try {
            sess.runner()
                    .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                    .addTarget("Pemail/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imei
        try {
            sess.runner()
                    .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                    .addTarget("Pimei/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //device
        try {
            sess.runner()
                    .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevice/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pserialnumber
        try {
            sess.runner()
                    .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                    .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pmacaddress
        try {
            sess.runner()
                    .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                    .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Padvertiser
        try {
            sess.runner()
                    .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                    .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPinternal_dst
        try {
            sess.runner()
                    .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPads_dst
        try {
            sess.runner()
                    .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Panalytics_dst
        try {
            sess.runner()
                    .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Psns_dst
        try {
            sess.runner()
                    .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pdevelop_dst
        try {
            sess.runner()
                    .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        Tensor output_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).fetch("output").run().get(0).expect(Float.class);

        //    Tensor output_tensor = sess.runner().feed("Plocation/initial_value",input_Plocation).feed("Pemail/initial_value",input_Pemail).feed("Pdevice/initial_value",input_Pdevice).feed("Pimei/initial_value",input_Pimei).feed("Pserialnumber/initial_value",input_Pserialnumber).feed("Pmacaddress/initial_value",input_Pmacaddress).feed("Padvertiser/initial_value",input_Padvertiser).feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).fetch("output").run().get(0).expect(Float.class);
float aux_valor = output_tensor.floatValue();
        float aux_valor2 = input_advertiser.floatValue() * input_Padvertiser.floatValue();



        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo
       /* if (op_tensor_verde.floatValue() == 1) {
            if(op_tensor_naranja.floatValue() == 1 ){
                if(op_tensor_rojo.floatValue() == 1 ){
                    Nivel = " High";
                    Nivel_color = 2;
                }
                else {
                    Nivel = " Medium";
                    Nivel_color = 1;
                }
            }
            else {
                Nivel = " Low";
                Nivel_color = 0;
            }
        }

        */
        //colores:
        if (output_tensor.floatValue() < umbral) {
            Nivel = " Low";
            Nivel_color = 0;
        }
        if (output_tensor.floatValue() >= umbral) {
            Nivel = " High";
            Nivel_color = 2;
        }

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
    private int Funcionar_salida_Ant_umbral_Color(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser, float internal_dst, float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {
        float n_epochs = 1;

         //y
        String op_string;


        //First, create an input tensor:

        Tensor input_location = Tensor.create(location);
        Tensor input_email = Tensor.create(email);
        Tensor input_imei = Tensor.create(imei);
        Tensor input_device = Tensor.create(device);
        Tensor input_serialnumber = Tensor.create(serialnumber);
        Tensor input_macaddress = Tensor.create(macaddress);
        Tensor input_advertiser = Tensor.create(advertiser);
        Tensor input_internal_dst = Tensor.create(internal_dst);
        Tensor input_ads_dst = Tensor.create(ads_dst);
        Tensor input_sns_dst = Tensor.create(sns_dst);
        Tensor input_develop_dst = Tensor.create(develop_dst);
        Tensor input_analytics_dst = Tensor.create(analytics_dst);


        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);
        Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
        Tensor input_Pads_dst = Tensor.create(Pads_dst);
        Tensor input_Psns_dst = Tensor.create(Psns_dst);
        Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
        Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);


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


              try {
            sess.runner()
                    .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                    .addTarget("Plocation/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //email
        try {
            sess.runner()
                    .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                    .addTarget("Pemail/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imei
        try {
            sess.runner()
                    .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                    .addTarget("Pimei/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //device
        try {
            sess.runner()
                    .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevice/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pserialnumber
        try {
            sess.runner()
                    .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                    .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pmacaddress
        try {
            sess.runner()
                    .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                    .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Padvertiser
        try {
            sess.runner()
                    .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                    .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPinternal_dst
        try {
            sess.runner()
                    .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPads_dst
        try {
            sess.runner()
                    .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Panalytics_dst
        try {
            sess.runner()
                    .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Psns_dst
        try {
            sess.runner()
                    .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pdevelop_dst
        try {
            sess.runner()
                    .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        Tensor output_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).fetch("output").run().get(0).expect(Float.class);



        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo

        //colores:
        if (output_tensor.floatValue() < umbral) {
            Nivel = " Bajo";
            Nivel_color = 0;
        }
        if (output_tensor.floatValue() >= umbral) {
            Nivel = " Alto";
            Nivel_color = 2;
        }


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




        String recomendacion = "No hacer nada";
        if (op_tensor.floatValue() == 1) {
            recomendacion = "Revisar App";
        }


        return recogidaDatos;
    }



    private String trainPrueba(float features, int epochs){



        // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the â€˜train_opâ€™ graph operation defined in the graph to train the graph:
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


           //umbral_green
            Tensor op_tensor = sess.runner().feed("location_input",location_train).feed("email_input",email_train).feed("imei_input",imei_train).feed("device_input",device_train).feed("umbral",umbral_train).feed("Plocation",input_Plocation).feed("Pemail",input_Pemail).feed("Pdevice",input_Pdevice).feed("Pimei",input_Pimei).feed("Pserialnumber",input_Pserialnumber).feed("Pmacaddress",input_Pmacaddress).feed("Padvertiser",input_Padvertiser).fetch("output").run().get(0).expect(Float.class);

            //Toast.makeText(this, "Modelo entrenado correctamente!", Toast.LENGTH_SHORT).show();
            //MODIFICAR:


            ctr++;
        }
        return "Model Trained";
    }
public void llamar_a_guardarShared(){
        GuardarShared();
}

    // ENTRENAR ANT MONITOR:
    public static String train_Ant_test_static(float location, float email, float device, float imei, float serialnumber, float macaddress, float advertiser,float umbral5 , int epochs, float target, float internal_dst, float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {


        //First, create the tensors for the input and the labels:

        // Tensor y_train = Tensor.create(label);                  //output
        //Then, use the â€˜train_opâ€™ graph operation defined in the graph to train the graph:

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

            org.tensorflow.Tensor input_internal_dst = Tensor.create(internal_dst); //input
            org.tensorflow.Tensor input_ads_dst = Tensor.create(ads_dst); //input
            org.tensorflow.Tensor input_sns_dst = Tensor.create(sns_dst); //input
            org.tensorflow.Tensor input_develop_dst = Tensor.create(develop_dst);//input
            org.tensorflow.Tensor input_analytics_dst = Tensor.create(analytics_dst);//input

            org.tensorflow.Tensor umbral_train = Tensor.create(umbral5); //input


            Tensor input_Plocation = Tensor.create(Plocation);
            Tensor input_Pemail = Tensor.create(Pemail);
            Tensor input_Pimei = Tensor.create(Pimei);
            Tensor input_Pdevice = Tensor.create(Pdevice);
            Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
            Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
            Tensor input_Padvertiser = Tensor.create(Padvertiser);

            Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
            Tensor input_Pads_dst = Tensor.create(Pads_dst);
            Tensor input_Psns_dst = Tensor.create(Psns_dst);
            Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
            Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);



            Tensor y_train = Tensor.create(target);         //output, target
            Log.i("Salida: ", "out"+umbral5);


            sess.runner().feed("serialnumber_input", serialnumber_train).feed("macaddress_input", macaddress_train).feed("advertiser_input", advertiser_train).feed("location_input", location_train).feed("email_input", email_train).feed("imei_input", imei_train).feed("device_input", device_train).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).feed("target", y_train).addTarget("train").run();


            ArrayList<Tensor<?>> values = (ArrayList<Tensor<?>>) sess.runner().fetch("Plocation/read").fetch("Pemail/read").fetch("Pimei/read").fetch("Pdevice/read").fetch("Pserialnumber/read").fetch("Pmacaddress/read").fetch("Padvertiser/read").fetch("Pinternal_dst/read").fetch("Pads_dst/read").fetch("Panalytics_dst/read").fetch("Psns_dst/read").fetch("Pdevelop_dst/read").run();

            recogidaDatos.add(0,values.get(0).floatValue());
            recogidaDatos.add(1,values.get(1).floatValue());
            recogidaDatos.add(2,values.get(2).floatValue());
            recogidaDatos.add(3,values.get(3).floatValue());
            recogidaDatos.add(4,values.get(4).floatValue());
            recogidaDatos.add(5,values.get(5).floatValue());
            recogidaDatos.add(6,values.get(6).floatValue());

          Plocation = values.get(0).floatValue();
            Pemail = values.get(1).floatValue();
            Pimei = values.get(2).floatValue();
            Pdevice = values.get(3).floatValue();
            Pserialnumber = values.get(4).floatValue();
            Pmacaddress = values.get(5).floatValue();
            Padvertiser = values.get(6).floatValue();

            Pinternal_dst = values.get(7).floatValue();
            Pads_dst = values.get(8).floatValue();
            Panalytics_dst = values.get(9).floatValue();
            Psns_dst = values.get(10).floatValue();
            Pdevelop_dst = values.get(11).floatValue();

//            actualizo el valor de los datos tras entrenarse.
            //locaiton
            try {
                sess.runner()
                        .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                        .addTarget("Plocation/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //email
            try {
                sess.runner()
                        .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                        .addTarget("Pemail/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //imei
            try {
                sess.runner()
                        .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                        .addTarget("Pimei/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //device
            try {
                sess.runner()
                        .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                        .addTarget("Pdevice/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Pserialnumber
            try {
                sess.runner()
                        .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                        .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Pmacaddress
            try {
                sess.runner()
                        .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                        .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Padvertiser
            try {
                sess.runner()
                        .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                        .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //PPinternal_dst
            try {
                sess.runner()
                        .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                        .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //PPads_dst
            try {
                sess.runner()
                        .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                        .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Panalytics_dst
            try {
                sess.runner()
                        .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                        .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Psns_dst
            try {
                sess.runner()
                        .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                        .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Pdevelop_dst
            try {
                sess.runner()
                        .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                        .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                        .run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //


       //     Tensor salida_calculadacambios = Funcionar_salida_Ant_umbral_static(location, email, imei, device, serialnumber, macaddress, advertiser);
            Log.i("Salida  cambios: ", "out"+umbral5);

            Log.i("valor loc: ",""+Plocation);
            Log.i("valor em: ",""+Pemail);
            Log.i("valor imei: ",""+Pimei);
            Log.i("valor dev: ",""+Pdevice);
            Log.i("valor ser: ",""+Pserialnumber);
            Log.i("valor mac: ",""+Pmacaddress);
            Log.i("valor ad: ",""+Padvertiser);

            Log.i("valor int_dst: ",""+Pinternal_dst);
            Log.i("valor ad_dst: ",""+Pads_dst);
            Log.i("valor analytic_dst: ",""+Panalytics_dst);
            Log.i("valor sns_dst: ",""+Psns_dst);
            Log.i("valor develop_dst: ",""+Pdevelop_dst);

            //loc
            if(Plocation >900 ){
                Plocation = (float) 900;
            }
            if(Plocation<=-900) {
                Plocation =-900;
            }
            //Pdevelop_dst
            if(Pdevelop_dst >900 ){
                Pdevelop_dst = (float) 900;
            }
            if(Pdevelop_dst<=-900) {
                Pdevelop_dst =-900;
            }
            //Pads_dst
            if(Psns_dst >900 ){
                Psns_dst = (float) 900;
            }
            if(Psns_dst<=-900) {
                Psns_dst =-900;
            }
            //Pads_dst
            if(Panalytics_dst >900 ){
                Panalytics_dst = (float) 900;
            }
            if(Panalytics_dst<=-900) {
                Panalytics_dst =-900;
            }
            //Pads_dst
            if(Pads_dst >900 ){
                Pads_dst = (float) 900;
            }
            if(Pads_dst<=-900) {
                Pads_dst =-900;
            }
            //Pinternal_dst
            if(Pinternal_dst >900 ){
                Pinternal_dst = (float) 900;
            }
            if(Pinternal_dst<=-900) {
                Pinternal_dst =-900;
            }
            //Padvertiser
            if(Padvertiser >900 ){
                Padvertiser = (float) 900;
            }
            if(Padvertiser<=-900) {
                Padvertiser =-900;
            }
            //Pmacaddress
            if(Pmacaddress >900 ){
                Pmacaddress = (float) 900;
            }
            if(Pmacaddress<=-900) {
                Pmacaddress =-900;
            }
            //Pserialnumber
            if(Pserialnumber >900 ){
                Pserialnumber = (float) 900;
            }
            if(Pserialnumber<=-900) {
                Pserialnumber =-900;
            }
            //Pdevice
            if(Pdevice >900 ){
                Pdevice = (float) 900;
            }
            if(Pdevice<=-900) {
                Pdevice =-900;
            }
            //Pimei
            if(Pimei >900 ){
                Pimei = (float) 900;
            }
            if(Pimei<=-900) {
                Pimei =-900;
            }
            //email
            if(Pemail >900 ){
                Pemail = (float) 900;
            }
            if(Pemail<=-900) {
                Pemail =-900;
            }
            Log.i("valor loc: "," TRAS AJUSTAR TODO BIEN EN LIMITES ARRIBA Y ABAJO");
            Log.i("valor loc: ",""+Plocation);
            Log.i("valor em: ",""+Pemail);
            Log.i("valor imei: ",""+Pimei);
            Log.i("valor dev: ",""+Pdevice);
            Log.i("valor ser: ",""+Pserialnumber);
            Log.i("valor mac: ",""+Pmacaddress);
            Log.i("valor ad: ",""+Padvertiser);

            Log.i("valor int_dst: ",""+Pinternal_dst);
            Log.i("valor ad_dst: ",""+Pads_dst);
            Log.i("valor analytic_dst: ",""+Panalytics_dst);
            Log.i("valor sns_dst: ",""+Psns_dst);
            Log.i("valor develop_dst: ",""+Pdevelop_dst);



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
        //Then, use the â€˜train_opâ€™ graph operation defined in the graph to train the graph:
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
        textFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
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

    //si se hace lo de los grupos, serÃ­a GRUPO A: file1, GRUPO B: file2, GRUPO C: file3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeFileAnt() {
       textFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");

        try{
            FileOutputStream fos = new FileOutputStream(textFile);
            String mensaje = "Pesos modelo Ant \n";
       //     String mensaje_final = mensaje + "\n" + Plocation + "\n" + Pemail + "\n" + Pdevice + "\n" + Pimei+ "\n" + Pserialnumber + "\n" + Pmacaddress + "\n" + Padvertiser + "\n" +umbral_verde + "\n" + umbral_naranja + "\n" + umbral_rojo;
            String mensaje_final = mensaje + "\n" + Plocation + "\n" + Pemail + "\n" + Pdevice + "\n" + Pimei+ "\n" + Pserialnumber + "\n"
                    + Pmacaddress + "\n" + Padvertiser + "\n" +umbral_verde + "\n" + umbral_naranja + "\n" + umbral_rojo + "\n" + Pinternal_dst
                    + "\n" +Pads_dst + "\n" +Panalytics_dst+ "\n" + Psns_dst + "\n" +Pdevelop_dst + "\n" + numeroAppsAnalizasTotal + "\n" + numeroAppsDisagree;




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
            if (clasificacion_usuarios == "D"){
                fileD = textFile;
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

    //////////////////////////////////////////////////////////////////////////////////

    //si se hace lo de los grupos, sería GRUPO A: file1, GRUPO B: file2, GRUPO C: file3
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void readFileAnt() {
        //poner un fichero de verdad de descargar:
        // file_leer = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/file1prueba");
        file_leer = new File((getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/fichero_datos"+"global"+".ckpt").toString());  //nombre
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
            //destinations:
            String Pinternal_dst1 = fin.readLine();
            String Pads_dst1 = fin.readLine();
            String Panalytics_dst1 = fin.readLine();
            String Psns_dst1 = fin.readLine();
            String Pdevelop_dst1 = fin.readLine();


            //


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
            try { if (umbralverde1.charAt(1) != '.'){
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
            try { if (umbralnaranja1.charAt(1) != '.'){
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
            try { if (umbralrojo1.charAt(1) != '.'){
                float A1 = umbralrojo1.charAt(1) - '0';
                float A2 = umbralrojo1.charAt(0) - '0';
                A2 *= 10;
                umbralrojo2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            umbral_rojo = umbralrojo2;

            // saco internal
            float Pinternal_dst2 =  Pinternal_dst1.charAt(0)  - '0';;
            try { if (Pinternal_dst1.charAt(1) != '.'){
                float A1 = Pinternal_dst1.charAt(1) - '0';
                float A2 = Pinternal_dst1.charAt(0) - '0';
                A2 *= 10;
                Pinternal_dst2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pinternal_dst = Pinternal_dst2;

            // saco adv
            float Pads_dst2 =  Pads_dst1.charAt(0)  - '0';;
            try { if (Pads_dst1.charAt(1) != '.'){
                float A1 = Pads_dst1.charAt(1) - '0';
                float A2 = Pads_dst1.charAt(0) - '0';
                A2 *= 10;
                Pads_dst2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pads_dst = Pads_dst2;

            // saco analytics
            float Panalytics_dst2 =  Panalytics_dst1.charAt(0)  - '0';;
            try { if (Panalytics_dst1.charAt(1) != '.'){
                float A1 = Panalytics_dst1.charAt(1) - '0';
                float A2 = Panalytics_dst1.charAt(0) - '0';
                A2 *= 10;
                Panalytics_dst2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Panalytics_dst = Panalytics_dst2;

            // saco sns
            float Psns_dst2 =  Psns_dst1.charAt(0)  - '0';;
            try { if (Psns_dst1.charAt(1) != '.'){
                float A1 = Psns_dst1.charAt(1) - '0';
                float A2 = Psns_dst1.charAt(0) - '0';
                A2 *= 10;
                Psns_dst2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Psns_dst = Psns_dst2;

            // saco develop
            float Pdevelop_dst2 =  Pdevelop_dst1.charAt(0)  - '0';;
            try { if (Pdevelop_dst1.charAt(1) != '.'){
                float A1 = Pdevelop_dst1.charAt(1) - '0';
                float A2 = Pdevelop_dst1.charAt(0) - '0';
                A2 *= 10;
                Psns_dst2 = A1 + A2;}
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pdevelop_dst = Pdevelop_dst2;




            Log.i("Ver contenido ", "Se lee: "+titulo1+vacio1+Plocation1+Pemail1+Pdevice1+Pimei1+Pdevice1+Pimei1+Pserialnumber1+Pmacaddress1+Padvertiser1);
            Log.i("Ver contenido ", "Se lee: "+titulo1+vacio1+Plocation2+Pemail2+Pdevice2+Pimei2+Pdevice2+Pimei2+Pserialnumber2+Pmacaddress2+Padvertiser2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GuardarShared();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initializeGraph_check() {
          checkpointPrefix = Tensors.create((getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  "/checkpoint_actualizado_"+"global"+".ckpt").toString());
        modelctr++;
        //pongo el umbral al nivel normal, una vez entrenado el modelo
        umbral = 10;
        checkpointDir = getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        graph = new Graph();
        readFileAnt();
        sess = new Session(graph);
        InputStream inputStream;
        try {
            //lo cambio por ANT
            // inputStream = getAssets().open("graph5.pb"); //BUENO
            inputStream = getActivity().getAssets().open("graph_Ant_v17.pb"); //ANT
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
            Toast.makeText(getActivity(), "Checkpoint Found and Loaded!", Toast.LENGTH_SHORT).show();
            //  Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            sess.runner().addTarget("init").run();
            Log.i("Checkpoint: ", "Graph Initialized");
        }
    }




    //input umbral : umbral verde, umbral naranaja, umbral rojo
    public static Tensor Funcionar_salida_Ant_umbral_static(float location, float email, float imei, float device, float serialnumber, float macaddress, float advertiser, float internal_dst,  float ads_dst, float sns_dst, float analytics_dst, float develop_dst) {
        float n_epochs = 1;

         //y

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
        Tensor input_internal_dst = Tensor.create(internal_dst);
        Tensor input_ads_dst = Tensor.create(ads_dst);
        Tensor input_sns_dst = Tensor.create(sns_dst);
        Tensor input_develop_dst = Tensor.create(develop_dst);
        Tensor input_analytics_dst = Tensor.create(analytics_dst);


        Tensor input_umbral_verde = Tensor.create(umbral_verde);
        Tensor input_umbral_naranja = Tensor.create(umbral_naranja);
        Tensor input_umbral_rojo = Tensor.create(umbral_rojo);

        Tensor input_Plocation = Tensor.create(Plocation);
        Tensor input_Pemail = Tensor.create(Pemail);
        Tensor input_Pimei = Tensor.create(Pimei);
        Tensor input_Pdevice = Tensor.create(Pdevice);
        Tensor input_Pserialnumber = Tensor.create(Pserialnumber);
        Tensor input_Pmacaddress = Tensor.create(Pmacaddress);
        Tensor input_Padvertiser = Tensor.create(Padvertiser);
        Tensor input_Pinternal_dst = Tensor.create(Pinternal_dst);
        Tensor input_Pads_dst = Tensor.create(Pads_dst);
        Tensor input_Psns_dst = Tensor.create(Psns_dst);
        Tensor input_Pdevelop_dst = Tensor.create(Pdevelop_dst);
        Tensor input_Panalytics_dst = Tensor.create(Panalytics_dst);





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


          try {
            sess.runner()
                    .feed("Plocation/initial_value", input_Plocation) // myvar.initializer.inputs[1].name
                    .addTarget("Plocation/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //email
        try {
            sess.runner()
                    .feed("Pemail/initial_value", input_Pemail) // myvar.initializer.inputs[1].name
                    .addTarget("Pemail/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imei
        try {
            sess.runner()
                    .feed("Pimei/initial_value", input_Pimei) // myvar.initializer.inputs[1].name
                    .addTarget("Pimei/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //device
        try {
            sess.runner()
                    .feed("Pdevice/initial_value", input_Pdevice) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevice/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pserialnumber
        try {
            sess.runner()
                    .feed("Pserialnumber/initial_value", input_Pserialnumber) // myvar.initializer.inputs[1].name
                    .addTarget("Pserialnumber/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pmacaddress
        try {
            sess.runner()
                    .feed("Pmacaddress/initial_value", input_Pmacaddress) // myvar.initializer.inputs[1].name
                    .addTarget("Pmacaddress/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Padvertiser
        try {
            sess.runner()
                    .feed("Padvertiser/initial_value", input_Padvertiser) // myvar.initializer.inputs[1].name
                    .addTarget("Padvertiser/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPinternal_dst
        try {
            sess.runner()
                    .feed("Pinternal_dst/initial_value", input_internal_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pinternal_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PPads_dst
        try {
            sess.runner()
                    .feed("Pads_dst/initial_value", input_ads_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pads_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Panalytics_dst
        try {
            sess.runner()
                    .feed("Panalytics_dst/initial_value", input_analytics_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Panalytics_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Psns_dst
        try {
            sess.runner()
                    .feed("Psns_dst/initial_value", input_Psns_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Psns_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Pdevelop_dst
        try {
            sess.runner()
                    .feed("Pdevelop_dst/initial_value", input_develop_dst) // myvar.initializer.inputs[1].name
                    .addTarget("Pdevelop_dst/Assign")           // myvar.initializer.name
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        Tensor output_tensor = sess.runner().feed("location_input",input_location).feed("email_input",input_email).feed("imei_input",input_imei).feed("device_input",input_device).feed("serialnumber_input",input_serialnumber).feed("macaddress_input",input_macaddress).feed("advertiser_input",input_advertiser).feed("internal_dst_input",input_internal_dst).feed("ads_dst_input",input_ads_dst).feed("analytics_dst_input",input_analytics_dst).feed("sns_dst_input",input_sns_dst).feed("develop_dst_input",input_develop_dst).fetch("output").run().get(0).expect(Float.class);


        String recomendacion = "No hacer nada";
        String Nivel = " Bajo";
        int Nivel_color = 0; //0 es verde, 1 naranja, 2 rojo

        //colores:
        if (output_tensor.floatValue() < umbral) {
            Nivel = " Bajo";
            Nivel_color = 0;
        }
        if (output_tensor.floatValue() >= umbral) {
            Nivel = " Alto";
            Nivel_color = 2;
        }


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



        return output_tensor;
    }
    //varios metodos: upload, download.... horas.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadModelmetodo() {
        //  COMENTARIO PRUEBA BBDD 22-02-2021

        writeFileAnt();
        MyAsyncTask uploadWeights = new MyAsyncTask(getActivity(), file,  dirIP,"","uploadWeights", new MyAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                Log.i("Output: uploadWeights", result);
                Toast.makeText(getActivity(), "7 Weights Successfully Uploaded", Toast.LENGTH_SHORT).show();



            }
        });
        uploadWeights.execute();

        // cambiar activity: mainactivity.this por getActivity(). pero leugo da error el MyAsyncTask

        // Toast.makeText(MainActivity.this, "7 Weights updated", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "7 Weights updated", Toast.LENGTH_SHORT).show();
        GuardarShared();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getModelmetodo() {
        MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(getActivity(), file, dirIP,"", "ismodelUpdated", new MyAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String result) {
                //If True, get Global Model
                MyAsyncTask getGlobalModel = new MyAsyncTask(getActivity(), file,  dirIP,"","getModel", new MyAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(String result) {
                        Log.i("Output: GetGlobalModel", result);
                    }
                });
                getGlobalModel.execute();
            //    Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                Log.i("Output: isModelUpdated", "Done");

            }
        });
        isGlobalModelUpdated.execute();
        file_descargado = new File(getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "graph_pesos");
         isModelUpdated = true;
        GuardarShared();

    }

    class ExampleRunnable implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)




        @Override
        public void run(){

               // ExampleRunnable runnable = new ExampleRunnable();
               // new Thread(runnable).start();


                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = getActivity().registerReceiver(null, ifilter);

                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                Calendar rightNow = Calendar.getInstance();
                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

                int currentHourIn12Format = rightNow.get(Calendar.HOUR); // return the hour in 12 hrs format (ranging from 0-11)
            Calendar c = Calendar.getInstance();
            int min = rightNow.get(Calendar.MINUTE);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


                if((isCharging)&&(currentHourIn24Format == ((int) 3))&&(unaVez)&&(2<dayOfWeek)&&(dayOfWeek<6)){ /// si estÃ¡ cargando y son las 2 de la maÃ±ana sube una actualizaciÃ³n al servidor, lunes a jueves
                  //  uploadModelmetodo();
                    upload_bool = true;
                    unaVez=false;
                    GuardarShared();

                  //activar servidor:
                    MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(getActivity(), file, dirIP, "", "ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            //If True, get Global Model

                            //  Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                            Log.i("Output: isModelUpdated", "Done");

                        }
                    });
                    isGlobalModelUpdated.execute();


                            // yourMethod();

                            //subir pesos actualizados.
                            writeFileAnt();
                            MyAsyncTask uploadWeights = new MyAsyncTask(getActivity(), file, dirIP,"","uploadWeights", new MyAsyncTask.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    Log.i("Output: uploadWeights", result);
                                    //  Toast.makeText(getActivity(), "7 Weights Successfully Uploaded", Toast.LENGTH_SHORT).show();

                                }
                            });
                            uploadWeights.execute();
                            GuardarShared();
                            Log.i(TAG, "9999999999999999999999999999          UPLOADED!!");





                }
                if((isCharging)&&(currentHourIn24Format == ((int) 5))&&(unaVez2)&&((2>=dayOfWeek)||(dayOfWeek>=6))){  /// si estÃ¡ cargando y son las 3 de la maÃ±ana descarga una actualizaciÃ³n del servidor, 7 es sabado. RESTO
                   // getModelmetodo();
                    download_bool = true;
                    unaVez2= false;
                    GuardarShared();

                    //activar servidor:
                    MyAsyncTask isGlobalModelUpdated2 = new MyAsyncTask(getActivity(), file, dirIP, "", "ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            //If True, get Global Model

                            //  Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                            Log.i("Output: isModelUpdated", "Done");

                        }
                    });
                    isGlobalModelUpdated2.execute();



                            // yourMethod();

                            //descargar modelo mejorado:
                            MyAsyncTask isGlobalModelUpdated = new MyAsyncTask(getActivity(), file, dirIP,"","ismodelUpdated", new MyAsyncTask.AsyncResponse() {
                                @Override
                                public void processFinish(String result) {
                                    //If True, get Global Model
                                    MyAsyncTask getGlobalModel = new MyAsyncTask(getActivity(), file,dirIP, "","getModel", new MyAsyncTask.AsyncResponse() {
                                        @Override
                                        public void processFinish(String result) {
                                            Log.i("Output: GetGlobalModel", result);
                                        }
                                    });
                                    getGlobalModel.execute();
                                  //  Toast.makeText(getActivity(), "Fetched Global Model Successfully", Toast.LENGTH_SHORT).show();
                                    Log.i("Output: isModelUpdated", "Done");

                                }
                            });
                            isGlobalModelUpdated.execute();
                            file_descargado = new File(getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "graph_pesos");
                                 isModelUpdated = true;
                            GuardarShared();



                        }




            if(currentHourIn24Format == ((int) 6)){ /// si estÃ¡ cargando y son las 3 de la maÃ±ana descarga una actualizaciÃ³n del servidor
                  unaVez=true;
             unaVez2= true;
               // unaVez=false;

            }
            Log.i(TAG,"hora= "+currentHourIn24Format + "min" + min );


            }

        }


    public class FetchCategoryTask extends AsyncTask<Void, Void, Void> {

        private final String TAG = MasInformacion.FetchCategoryTask.class.getSimpleName();
        private PackageManager pm;
        //private ActivityUtil mActivityUtil;

        @Override
        protected Void doInBackground(Void... errors) {
            String category;
            pm = getActivity().getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            Iterator<ApplicationInfo> iterator = packages.iterator();
            //  while (iterator.hasNext()) {
            // ApplicationInfo packageInfo = iterator.next();
         //   String query_url = "https://play.google.com/store/apps/details?id=com.imo.android.imoim";  //GOOGLE_URL + packageInfo.packageName;
            int num_apps = listItems2.size();
            for(int i=0; i<num_apps; i++) {
   try {
       if (i == 0) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(0) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux0 = category;
       }
       if (i == 1) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(1) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux1 = category;
       }
       if (i == 2) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(2) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux2 = category;
       }
       if (i == 3) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(3) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux3 = category;
       }
       if (i == 4) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(4) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux4 = category;
       }
       if (i == 5) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(5) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux5 = category;
       }
       if (i == 6) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(6) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux6 = category;
       }
       if (i == 7) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(7) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux7 = category;
       }
       if (i == 8) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(8) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux8 = category;
       }
       if (i == 9) {
           String query_url = GOOGLE_URL + lista_mapa_nombrestags.get(9) + URL_ESPAÑOL;
           Log.i(TAG, query_url);
           category = getCategory(query_url);
           Log.e("CATEGORY", category);
           aux9 = category;
       }

   } catch (Exception e) {
       e.printStackTrace();
   }

            }

            // store category or do something else
            //}
            return null;
        }


        public String getCategory(String query_url) {

            try {
                Document doc = Jsoup.connect(query_url).get();
                Elements link = doc.select("a[class=\"hrTbp R8zArc\"]");
                return link.text();
            } catch (Exception e) {
                Log.e("DOc", e.toString());
            }return "ERROR";
        }

    }
    public void PlayStore0 (){
        error = true;
        splited0 = aux0.split("\\s+");
        longitud0 = splited0.length;

        categoria0 = splited0[longitud0-1];

        Log.i("ULTIMA PALABRA0", ":"+categoria0);
        try {
            recomendacion = mapa_categorias.get(categoria0);
            error = false;

        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore1 (){
        error = true;
        try {
        splited1 = aux1.split("\\s+");
        longitud1 = splited1.length;
        categoria1 = splited1[longitud1-1];
        Log.i("ULTIMA PALABRA1", ":"+categoria1);

            recomendacion = mapa_categorias.get(categoria1);
            error = false;

        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore2 (){
        error = true;
        try {
            splited2 = aux2.split("\\s+");
        longitud2 = splited2.length;
        categoria2 = splited2[longitud2-1];


                recomendacion = mapa_categorias.get(categoria2);
                error = false;


        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore3 (){
        error = true;
        try {
        splited3 = aux3.split("\\s+");
        longitud3 = splited3.length;

        categoria3 = splited3[longitud3-1];

            recomendacion = mapa_categorias.get(categoria3);
            error = false;
        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore4 (){
        error = true;
        try {
            splited4 = aux4.split("\\s+");
        longitud4 = splited4.length;

        categoria4 = splited4[longitud4-1];


                recomendacion = mapa_categorias.get(categoria4);
                error = false;


        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore5 (){
        error = true;
        try {
        splited5 = aux5.split("\\s+");
        longitud5 = splited5.length;

        categoria5 = splited5[longitud5-1];

            recomendacion = mapa_categorias.get(categoria5);
            error = false;
        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore6 (){
        error = true;
        try {
        splited6 = aux6.split("\\s+");
        longitud6 = splited6.length;

        categoria6 = splited6[longitud6-1];

            recomendacion = mapa_categorias.get(categoria6);
            error = false;
        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore7 (){
        error = true;
        try {
        splited7 = aux7.split("\\s+");
        longitud7 = splited7.length;

        categoria7 = splited7[longitud7-1];

            recomendacion = mapa_categorias.get(categoria7);
            error = false;
        } catch (Exception e) {

            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore8 (){
        error = true;
        try {
        splited8 = aux8.split("\\s+");
        longitud8 = splited8.length;

        categoria8 = splited8[longitud8-1];

            recomendacion = mapa_categorias.get(categoria8);
            error = false;
        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void PlayStore9 (){
        error = true;
        try {
        splited9 = aux9.split("\\s+");
        longitud9 = splited9.length;

        categoria9 = splited9[longitud9-1];


            recomendacion = mapa_categorias.get(categoria9);
            error = false;
        } catch (Exception e) {
            error = true;
            recomendacion = "error";
            e.printStackTrace();

        }
        finally {
            if(recomendacion == null) {
                recomendacion = "Have not been found similar apps";
            }
        }

        lista_recomendaciones.add(recomendacion);
    }
    public void show(){
        //prueba guardar:

        //mirar  bien::

        //


        mapa_nombres.size();
        for (int i=0; i<mapa_nombres.size(); i++){
            //hay que cambiar el color del borde segÃºn sea el nivel. ver como hacerlo.

            listItems2.get(i).setResumen(Funcionar_salida_Ant_umbral_lista_Dentro(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i), lista_internal_dst.get(i), lista_ads_dst.get(i), lista_sns_dst.get(i), lista_analytics_dst.get(i), lista_develop_dst.get(i))); //devuleve el texto de dentro
            listItems2.get(i).setValor_nivel( Funcionar_salida_Ant_umbral_lista_Nivel(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i), lista_internal_dst.get(i), lista_ads_dst.get(i), lista_sns_dst.get(i), lista_analytics_dst.get(i), lista_develop_dst.get(i))); //devuelve nivel: bajo, medio, etc
            listItems2.get(i).setColor(Funcionar_salida_Ant_umbral_Color(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i), lista_internal_dst.get(i), lista_ads_dst.get(i), lista_sns_dst.get(i), lista_analytics_dst.get(i), lista_develop_dst.get(i)));  //devuleve el color: 0, 1,2
         //   listItems2.get(i).setNombre_app(lista_nombres1.get(i));  //poner el nombre de verdad
         //   listItems2.get(i).setNombre_app(lista_mapa_nombres.get(i));  //poner el nombre de verdad???? test 22-03-20
       //     listItems2.get(i).setRecomendacion(Funcionar_salida_Ant_umbral_lista_Dentro_destino(lista_location.get(i), lista_email.get(i),lista_device.get(i),lista_imei.get(i),lista_serialnumber.get(i),lista_macaddress.get(i),lista_advertiser.get(i), lista_internal_dst.get(i), lista_ads_dst.get(i), lista_sns_dst.get(i), lista_analytics_dst.get(i), lista_develop_dst.get(i))); //devuleve el texto de dentro
            Map<String, String> mapa_aux = mapa_tipos_servidores;
            ArrayList<String> lsita_aux = lista_tipos_servidores;
            //lista de url_button
            lista_url_button.add(i,mapa_url_compañias.get(lista_mapa_nombrestags.get(i)));
            listItems2.get(i).setRecomendacion("Data destination: "+mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i))); //devuleve el texto de dentro

            listItems2.get(i).setFoto(mapa_urlImage.get(lista_mapa_nombres.get(i)));
            //PARA PREGUNTAR A GOOGLEPLAY LA CATEGORIA LO COMENTO 10-03-2021
            /*
       try {
           listItems2.get(i).setRecomendacion("Try better: " + lista_recomendaciones.get(i));     //por si el servidor de google va lento
       } catch (Exception e) {
           e.printStackTrace();
       }

             */
            //test
           // listItems2.get(i).setRecomendacion("Try better :");
            //PARA PREGUNTAR A GOOGLEPLAY LA CATEGORIA LO COMENTO 10-03-2021
            //  Log.i("valor recomendacion :","."+lista_recomendaciones.get(i));
            listItems5.add(listItems2.get(i));
            //crear un lvItems 2, con todo bien ???

        }

        AntMonitorMainActivity.antMonitorSwitch.setChecked(true); //enciende
        //  lvItems.setVisibility(View.VISIBLE);                  /// LA BUENA ES ESTA
        lvItems2.setVisibility(View.VISIBLE);
       // info.setVisibility(View.VISIBLE);   //COMENTADO 13/04
        //   getModel.setVisibility(View.VISIBLE);
        //   upload.setVisibility(View.VISIBLE);
        Y.setVisibility(View.VISIBLE);

        //////////////////////////////
        reportsListView.setVisibility(View.GONE);
        Apps_analizadas.setVisibility(View.GONE);
        GuardarShared();
       // button1.setClickable(false);  //11-03-2021 test
     //   mostrar_resumen_final.setClickable(false);
      //  button1.setClickable(false);

         Map<String,Entidad> lista_app_final_vacia = new HashMap<String,Entidad>();


    }

    private ArrayList<Entidad> GetArrayItemsReal_06(){


        Set<String> keys = lista_apps_analizadas_final.keySet();
        ArrayList<Entidad> listAux = new ArrayList<>();
        Entidad auxent =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
        Entidad auxent0 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
        Entidad auxent1 =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
        Entidad auxent2 =new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");
        Entidad auxent3 = new Entidad("Agree", "Disagree", "Risk Level: ", "Bajo", 0,0,0,0, 0,0,0, "  Spotify","", 0, false, false, false, null, "", 0,0,0,0,0,"");

//intento nuevo
        int q=0;
        for (String k : keys) {
            q++;
            lista_apps_analizadas_final.get(k).setNombre_app(k); //pongo el nombre bien
            String aux = k;
            lista_apps_analizadas_final.get(k).setNombre_app(k); //para que los nombres se vean bien
            if(q==0) auxent0 = lista_apps_analizadas_final.get(k);
            if(q==1) auxent1 = lista_apps_analizadas_final.get(k);
            if(q==2) auxent2 = lista_apps_analizadas_final.get(k);
            if(q==3) auxent3 = lista_apps_analizadas_final.get(k);

        }

        for (int i=0; i<lista_mapa_nombres.size();i++){
            lista_apps_analizadas_final.get(lista_mapa_nombres.get(i)).setNombre_app(lista_mapa_nombres.get(i));


        }
        for (int i=0; i<lista_mapa_nombres.size();i++) {
            listItems2.add(lista_apps_analizadas_final.get(lista_mapa_nombres.get(i)));
        }
        for (int i = 0; i < lista_apps_analizadas_final.size() -2; i++){  //meto la primera y la segunda
            listItems4.add(listItems2.get(i));
        }
        auxent.setImei(1);
        auxent.setNombre_app(lista_mapa_nombres.get(lista_apps_analizadas_final.size()-2));         //pongo una entidad con nombre la de la 3 de lista app final
        auxent.setFoto(mapa_urlImage.get(auxent.getNombre_app()));
        auxent.setImei(1);
        listItems4.add(auxent);

        listItems4.add(listItems2.get(lista_apps_analizadas_final.size()-1));                        //la ultima es la ultima de listItem2
        listItems2 = listItems4;
        listItems.add(auxent0);
        listItems.add(auxent1);
        listItems.add(auxent2);
        listItems.add(auxent3);
        listAux = listItems;
        return listItems4;



    }

    public void DNS_parte2() {



        ArrayList<Drawable> lista_fotos = new ArrayList<>();
        float n_epochs = 1;



        boolean error = false;

        //PARA FUNCIONAR REAL SE CAMBIA LISTITEMS POR LISTITEMS2

        //PARA PREGUNTAR CATEGORIA A GOOGLEPLAY COMENDADO 10-03-2021
        //   tarea0.execute();
        int u = 0;
        for (int i = 0; i < mapa_nombres.size(); i++) {

            lista_location.add(listItems2.get(i).getLocation());
            lista_email.add(listItems2.get(i).getEmail());
            lista_device.add(listItems2.get(i).getDevice());
            lista_imei.add(listItems2.get(i).getImei());
            lista_serialnumber.add(listItems2.get(i).getSerialnumber());
            lista_macaddress.add(listItems2.get(i).getMacaddresss());
            lista_advertiser.add(listItems2.get(i).getAdvertiser());
            lista_nombres1.add(listItems2.get(i).getNombre_app());
            lista_ip.add(listItems2.get(i).getIp());

            //destinos. comprobar si coincide
            //internal
            //igual poner try. 14/04/2021
            try {

                if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) == STRING_INTERNAL_DST.charAt(2)) && (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) == STRING_INTERNAL_DST.charAt(3))) {
                    lista_internal_dst.add((float) 1.0);
                } else if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) != STRING_INTERNAL_DST.charAt(2)) || (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) != STRING_INTERNAL_DST.charAt(3))) {
                    lista_internal_dst.add((float) 0.0);
                }
                //ads
                if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) == STRING_ADVERTISING_DST.charAt(2)) && (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) == STRING_ADVERTISING_DST.charAt(3))) {
                    lista_ads_dst.add((float) 1.0);
                } else if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) != STRING_ADVERTISING_DST.charAt(2)) || (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) != STRING_ADVERTISING_DST.charAt(3))) {
                    lista_ads_dst.add((float) 0.0);
                }
                //aNALYTICS
                if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) == STRING_ANALYTICS_DST.charAt(2)) && (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) == STRING_ANALYTICS_DST.charAt(3))) {
                    lista_analytics_dst.add((float) 1.0);
                } else if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) != STRING_ANALYTICS_DST.charAt(2)) || (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) != STRING_ANALYTICS_DST.charAt(3))) {
                    lista_analytics_dst.add((float) 0.0);
                }
                //DEVELOP
                if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) == STRING_DEVELOPMENT_DST.charAt(2)) && (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) == STRING_DEVELOPMENT_DST.charAt(3))) {
                    lista_develop_dst.add((float) 1.0);
                } else if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) != STRING_DEVELOPMENT_DST.charAt(2)) || (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) != STRING_DEVELOPMENT_DST.charAt(3))) {
                    lista_develop_dst.add((float) 0.0);
                }
                //SOCIAL MEDIA
                if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) == STRING_SOCIAL_DST.charAt(2)) && (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) == STRING_SOCIAL_DST.charAt(3))) {
                    lista_sns_dst.add((float) 1.0);
                } else if ((mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(2) != STRING_SOCIAL_DST.charAt(2)) || (mapa_tipos_servidores.get(lista_mapa_nombrestags.get(i)).charAt(3) != STRING_SOCIAL_DST.charAt(3))) {
                    lista_sns_dst.add((float) 0.0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                lista_internal_dst.add((float) 0.0);
                lista_ads_dst.add((float) 1.0);
                lista_analytics_dst.add((float) 0.0);
                lista_develop_dst.add((float) 0.0);
                lista_sns_dst.add((float) 0.0);
                //mapa_tipo_servidores un Destionation Internal

            }




            url_categoria = GOOGLE_URL + lista_mapa_nombrestags.get(i) + URL_ESPAÑOL;

            TinyDB tinydb = new TinyDB(getActivity());
            //tinydb.putList("MyUsers", mUsersArray);
            if (listItems2.get(0).getNombre_app() != "  Spotify") {


                tinydb.putListDouble("lista_location", lista_location);
                tinydb.putListDouble("lista_email", lista_email);
                tinydb.putListDouble("lista_device", lista_device);
                tinydb.putListDouble("lista_imei", lista_imei);
                tinydb.putListDouble("lista_serialnumber", lista_serialnumber);
                tinydb.putListDouble("lista_macaddress", lista_macaddress);
                tinydb.putListDouble("lista_advertiser", lista_advertiser);
                tinydb.putListString("lista_nombres1", lista_nombres1);
                tinydb.putListString("lista_ip", lista_ip);

                tinydb.putListDouble("lista_internal_dst", lista_internal_dst);
                tinydb.putListDouble("lista_ads_dst", lista_ads_dst);
                tinydb.putListDouble("lista_analytics_dst", lista_analytics_dst);
                tinydb.putListDouble("lista_develop_dst", lista_develop_dst);
                tinydb.putListDouble("lista_sns_dst", lista_sns_dst);
            }

//loadMapEntidad();
            if ((listItems2.get(0).getResumen() == "") || (listItems2.get(0).getResumen() == null) || (listItems2.get(0).getNombre_app() == "  Spotify")) {


                lista_location = tinydb.getListDouble("lista_location");
                lista_email = tinydb.getListDouble("lista_email");
                lista_device = tinydb.getListDouble("lista_device");
                lista_imei = tinydb.getListDouble("lista_imei");
                lista_serialnumber = tinydb.getListDouble("lista_serialnumber");
                lista_macaddress = tinydb.getListDouble("lista_macaddress");
                lista_advertiser = tinydb.getListDouble("lista_advertiser");
                //       lista_nombres1 =tinydb.getListString("lista_nombres1");
                lista_ip = tinydb.getListString("lista_ip");
                lista_internal_dst = tinydb.getListDouble("lista_internal_dst");
                lista_ads_dst = tinydb.getListDouble("lista_ads_dst");
                lista_analytics_dst = tinydb.getListDouble("lista_analytics_dst");
                lista_develop_dst = tinydb.getListDouble("lista_develop_dst");
                lista_sns_dst = tinydb.getListDouble("lista_sns_dst");


            }


        }
    }



}
