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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.activity.uielements.PrivacyLeaksAppHistoryCursorLoader;
import edu.uci.calit2.federated.client.android.analysis.ActionReceiver;
import edu.uci.calit2.federated.client.android.database.PrivacyDB;
import edu.uci.calit2.federated.client.android.fragment.FederatedFragment;
import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;

/**
 * @author Hieu Le
 */
public class LeaksAppHistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String INTENT_EXTRA_APP_NAME = "INTENT_APP_NAME";
    public static final String INTENT_EXTRA_PACKAGE_NAME = "INTENT_PACKAGE_NAME";
    public static final String INTENT_EXTRA_APP_NAME2 = "INTENT_APP_NAME2";
    public static final String INTENT_EXTRA_PACKAGE_NAME2 = "INTENT_PACKAGE_NAME2";

    private SimpleCursorAdapter mAdapter;
    private static final int LOADER_APP_HISTORY = 0;
    private String pkgName;

    BroadcastReceiver leaksChangedReceiver;
    private IntentFilter leaksFilter;
    private boolean acabar = false;
    private boolean acabar1 = false;
    public static Map<String, Entidad> lista_apps_analizadas_final_guardadas = new HashMap<String, Entidad>();
    public static String appName_aux;

    public static String appName;
    public static String appName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaks_app_history);                   //aqui estan todos los filtraciones de la app que lo llama
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView cargando = (TextView) findViewById(R.id.cargando);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        pkgName = intent.getStringExtra(INTENT_EXTRA_PACKAGE_NAME);        //com.facemoji.lite.xiaomi
        appName = intent.getStringExtra(INTENT_EXTRA_APP_NAME);     // Keyboard for Xiaomi                          //nombre de la app que lo llama : appNAme
        String u = INTENT_EXTRA_APP_NAME2;
        String a = INTENT_EXTRA_APP_NAME;

        appName2 = intent.getStringExtra(INTENT_EXTRA_APP_NAME2);
        String pkgName2 = intent.getStringExtra(INTENT_EXTRA_PACKAGE_NAME2);        //com.facemoji.lite.xiaomi

        appName_aux = appName;

        if (appName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(appName + " " + getResources().getString(R.string.leaks_history));                              //aqui se pone el título APp nombre + Privacy exposrure history
        }

        // set back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView historyList = (ListView) findViewById(R.id.leak_history_list);      //vista de la lista de item: cada item: Advertiser ID; el numero, , fecha y hora, y si se hashea o no
        //

        //

        // the columns refer to the data that will be coming from the cursor and how it ties to the layout elements
        String[] fromColumns = {PrivacyDB.COLUMN_PII_LABEL, PrivacyDB.COLUMN_ACTION, PrivacyDB.COLUMN_TIME, PrivacyDB.COLUMN_PII_VALUE, PrivacyDB.COLUMN_ACTION, PrivacyDB.COLUMN_ACTION, PrivacyDB.COLUMN_ACTION, PrivacyDB.COLUMN_ACTION};
        int[] toViews = {R.id.filter_label, R.id.filter_type, R.id.timestamp, R.id.filter_value, R.id.filter_icon_allow, R.id.filter_icon_hash, R.id.filter_icon_block, R.id.filter_icon_unknown}; // The TextViews in list_item_privacy_reports
        ///


        ////
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item_privacy_application_leak_history, null,         //muestra el logo, el dato la hora y asi como se ve en la app de una fomra más bonita
                fromColumns, toViews, 0);
        FederatedFragment.Numero_apps = 0;
        PrivacyLeaksReportFragment.Numero_apps = 0;

        // Used to post-process the data for a more user-friendly display
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {                               //filter_label es : email, deviceID, etc. IGUAL COMO ESTO DE ABAJO PERO CON COLUMN_PII_LABEL

            public boolean setViewValue(View toView, Cursor cursor, int columnIndex) {

                if (toView instanceof TextView && columnIndex == cursor.getColumnIndex(PrivacyDB.COLUMN_ACTION)) {
                    TextView textView = (TextView) toView;
                    String action = cursor.getString(columnIndex);             //la acción: hash block y asi.

                    //
                //    String RemoteIP_test = cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP));
                    String LABEL_TEST = cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL));
                    Log.i("valor label: ", LABEL_TEST);
                    String app0 = PrivacyLeaksReportFragment.lista_mapa_nombres.get(0);
                    char app_char = app0.charAt(0);
                    char app_char2 = app0.charAt(1);
                    String app1 = PrivacyLeaksReportFragment.lista_mapa_nombres.get(1);
                    String app2 = PrivacyLeaksReportFragment.lista_mapa_nombres.get(2);
                    String app3 = PrivacyLeaksReportFragment.lista_mapa_nombres.get(3);
                    Log.i("valor label: ", app0);
                    Log.i("valor label: ", app1);
                    Log.i("valor label: ", app2);
                    Log.i("valor label: ", ""+ app_char+ app_char2);

                    boolean igual = (PrivacyLeaksReportFragment.lista_mapa_nombres.get(0) ==appName_aux);
                    Log.i("valor label: ", "es true o no:"+(igual));
                    int i = PrivacyLeaksReportFragment.Numero_apps;
                  //  PrivacyLeaksReportFragment.list_app_test.add(new Entidad("Agree", "Disagree", "Level: ", "Bajo", 0, 0, 0, 0, 0, 0, 0, "  Spotify", "", 0, false, false, false, null, "")); //comentado 07-03-2021
                    //  FederatedFragment.list_app_test.get(i).setNombre_app(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_APP)));
                    // FederatedFragment.list_app_test.get(i).setNombre_app(appName);
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(0).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(0).charAt(1) ==appName_aux.charAt(1)))){




                    PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.Entidad0.setNombre_app(appName_aux);
                    if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                        PrivacyLeaksReportFragment.Entidad0.setImei(1);
                     //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }
                    if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                        PrivacyLeaksReportFragment.Entidad0.setDevice(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }
                    if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                        PrivacyLeaksReportFragment.Entidad0.setEmail(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }

                    if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                        PrivacyLeaksReportFragment.Entidad0.setAdvertiser(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }
                    if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                        PrivacyLeaksReportFragment.Entidad0.setSerialnumber(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }
                    if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                        PrivacyLeaksReportFragment.Entidad0.setMacaddresss(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }
                    if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                        PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                        PrivacyLeaksReportFragment.Entidad0.setLocation(1);
                        //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                    }

                      //

                    }

try{
                //entidad 2
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(1).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(1).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad1.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad1.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad1.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad1.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad1.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad1.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad1.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad1.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        //

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    try{
                //entidad 3
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(2).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(2).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad2.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad2.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad2.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad2.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad2.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad2.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad2.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad2.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        //

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    try{
                //entidad 4
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(3).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(3).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad3.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad3.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad3.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad3.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad3.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad3.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad3.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //entidad 5
                    try {
                        if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(4).charAt(0) == appName_aux.charAt(0)) && ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(4).charAt(1) == appName_aux.charAt(1)))) {


                            PrivacyLeaksReportFragment.Entidad4.setNombre_app(appName_aux);
                            PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                            if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                                PrivacyLeaksReportFragment.Entidad4.setImei(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                            if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                                PrivacyLeaksReportFragment.Entidad4.setDevice(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                            if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                                PrivacyLeaksReportFragment.Entidad4.setEmail(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }

                            if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                                PrivacyLeaksReportFragment.Entidad4.setAdvertiser(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                            if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                                PrivacyLeaksReportFragment.Entidad4.setSerialnumber(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                            if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                                PrivacyLeaksReportFragment.Entidad4.setMacaddresss(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                            if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                                PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                                PrivacyLeaksReportFragment.Entidad4.setLocation(1);
                                //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                            }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                            //

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try{
                    //entidad 6
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(5).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(5).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad5.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad5.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad5.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad5.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad5.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad5.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad5.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad5.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    try{
                //entidad 7
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(6).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(6).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad6.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad6.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad6.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad6.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad6.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad6.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad6.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad6.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
                    try{
            //entidad 8
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(7).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(7).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad7.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad7.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad7.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad7.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad7.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad7.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad7.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad7.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
                    try{
        //entidad 9
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(8).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(8).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad8.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad8.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad8.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad8.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad8.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad8.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad8.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad8.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
    } catch (Exception e) {
        e.printStackTrace();
    }
                    try{
    //entidad 10
                    if ((PrivacyLeaksReportFragment.lista_mapa_nombres.get(9).charAt(0) ==appName_aux.charAt(0))&&((PrivacyLeaksReportFragment.lista_mapa_nombres.get(9).charAt(1) ==appName_aux.charAt(1)))){



                        PrivacyLeaksReportFragment.Entidad9.setNombre_app(appName_aux);
                        PrivacyLeaksReportFragment.list_app_test.get(0).setNombre_app(appName_aux);
                        if ("IMEI".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setImei(1);
                            PrivacyLeaksReportFragment.Entidad9.setImei(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Device ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setDevice(1);
                            PrivacyLeaksReportFragment.Entidad9.setDevice(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Email".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setEmail(1);
                            PrivacyLeaksReportFragment.Entidad9.setEmail(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }

                        if ("Advertiser ID".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setAdvertiser(1);
                            PrivacyLeaksReportFragment.Entidad9.setAdvertiser(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Serial Number".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setSerialnumber(1);
                            PrivacyLeaksReportFragment.Entidad9.setSerialnumber(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("MAC Address".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setMacaddresss(1);
                            PrivacyLeaksReportFragment.Entidad9.setMacaddresss(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                        if ("Location".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad9.setLocation(1);
                            //   PrivacyLeaksReportFragment.lista_remoteIP_test.add(cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP)));
                        }
                      /*  if ("DEFAULT_PII_VALUE__LOCATION".charAt(0) == cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_PII_LABEL)).charAt(0)) {
                            PrivacyLeaksReportFragment.list_app_test.get(0).setLocation(1);
                            PrivacyLeaksReportFragment.Entidad3.setLocation(1);
                        }

                       */
                        //

                    }
} catch (Exception e) {
        e.printStackTrace();
        }




                    //
                    if (action.equals(ActionReceiver.ACTION_ALLOW)) {
                        action = "Information allowed to be exposed";
                    } else if (action.equals(ActionReceiver.ACTION_DENY)) {
                        action = "Packet Blocked";
                    } else if (action.equals(ActionReceiver.ACTION_HASH)) {
                        action = "Information was Hashed";
                    } else {
                        action = "Unknown";
                    }
         //           String RemoteIP_test = cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP));

                    textView.setText(action);                 // se pone si se hash, o se block o que se hace para cada filtracion
                    //acabar = true;
                    //poner un boolean para que empiece el siguiente:
                    acabar = true;
                    PrivacyLeaksReportFragment.nombres = PrivacyLeaksReportFragment.lista_mapa_nombres_vacia; //nuevo
                  finish();
            
                    return true;
                } else if (toView instanceof ImageView && columnIndex == cursor.getColumnIndex(PrivacyDB.COLUMN_ACTION)) {
                    ImageView imageView = (ImageView) toView;
                    String action = cursor.getString(columnIndex);
                    if (action.equals(ActionReceiver.ACTION_ALLOW)) {
                        if (imageView.getId() == R.id.filter_icon_allow) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    } else if (action.equals(ActionReceiver.ACTION_DENY)) {
                        if (imageView.getId() == R.id.filter_icon_block) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    } else if (action.equals(ActionReceiver.ACTION_HASH)) {
                        if (imageView.getId() == R.id.filter_icon_hash) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (imageView.getId() == R.id.filter_icon_unknown) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
           //         String RemoteIP_test = cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP));
                    //podria ir aqui
                    try {
                        PrivacyLeaksReportFragment.lista_apps_analizadas_final2.put(appName_aux, PrivacyLeaksReportFragment.list_app_test.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /////////
                    return true;
                } else if (toView instanceof TextView && columnIndex == cursor.getColumnIndex(PrivacyDB.COLUMN_TIME)) {
                    TextView textView = (TextView) toView;
                    SimpleDateFormat df = new SimpleDateFormat("MMM d hh:mm a");
                    df.setTimeZone(TimeZone.getDefault());
                    String result = df.format(cursor.getLong(columnIndex));
                    textView.setText(result);                 //pone la hora
                    //podria ir aqui
                //    String RemoteIP_test = cursor.getString(cursor.getColumnIndex(PrivacyDB.COLUMN_REMOTE_IP));
                    try {
                        PrivacyLeaksReportFragment.lista_apps_analizadas_final2.put(appName_aux, PrivacyLeaksReportFragment.list_app_test.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ///////// return true;
                    //podria ir qui
                }
                 //podria aqui.


                return false;
            }
        });



        historyList.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(LOADER_APP_HISTORY, null, this);

        leaksChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String pkgLeaked = intent.getStringExtra(PrivacyDB.COLUMN_APP);
                // reset the view when there is new data
                if (intent.getAction().equals(PrivacyDB.DB_LEAK_CHANGED) && pkgName.equals(pkgLeaked)) {
                    refreshView();
                }
            }
        };

        leaksFilter = new IntentFilter(PrivacyDB.DB_LEAK_CHANGED);


    }


//}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new PrivacyLeaksAppHistoryCursorLoader(this, pkgName);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void refreshView() {
        getSupportLoaderManager().restartLoader(LOADER_APP_HISTORY, null, this);
    }

    public void onResume() {
        super.onResume();
        refreshView();
        LocalBroadcastManager.getInstance(this).registerReceiver(leaksChangedReceiver, leaksFilter);
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(leaksChangedReceiver);
    }

    public void saveMap(Map<String,Entidad> inputMap){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }
    }

    public Map<String,Entidad> loadMap(){
        Map<String,Entidad> outputMap = new HashMap<String,Entidad>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
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
}
