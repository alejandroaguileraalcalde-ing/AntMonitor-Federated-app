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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.analysis.AnalysisHelper;
import edu.uci.calit2.federated.client.android.device.Installation;
import edu.uci.calit2.federated.client.android.fragment.AboutFragment;
import edu.uci.calit2.federated.client.android.fragment.ContributeLogsFragment;
import edu.uci.calit2.federated.client.android.fragment.ContributeSettingsFragment;
import edu.uci.calit2.federated.client.android.fragment.FederatedFragment;
import edu.uci.calit2.federated.client.android.fragment.GSMainAboutFragment;
import edu.uci.calit2.federated.client.android.fragment.HelpFragment;
import edu.uci.calit2.federated.client.android.fragment.MasInformacionFragment;
import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;
import edu.uci.calit2.federated.client.android.fragment.PrivacySettingsFragment;
import edu.uci.calit2.federated.client.android.fragment.RealTimeFragment;
import edu.uci.calit2.federated.client.android.util.PermissionHelper;
import edu.uci.calit2.federated.client.android.util.PreferenceTags;
import edu.uci.calit2.antmonitor.lib.AntMonitorActivity;
import edu.uci.calit2.antmonitor.lib.logging.PacketConsumer;
import edu.uci.calit2.antmonitor.lib.vpn.IncPacketFilter;
import edu.uci.calit2.antmonitor.lib.vpn.OutPacketFilter;
import edu.uci.calit2.antmonitor.lib.vpn.ReminderBroadcast;
import edu.uci.calit2.antmonitor.lib.vpn.VpnController;
import edu.uci.calit2.antmonitor.lib.vpn.VpnState;

//import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

//import com.google.android.gms.common.util.ArrayUtils;

import java.util.Calendar;

/**
 * @author Hieu Le and Alejandro Aguilera Alcalde 2021
 */
public class AntMonitorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RealTimeFragment.OnFragmentInteractionListener,
        PrivacyLeaksReportFragment.OnFragmentInteractionListener,
        ContributeLogsFragment.OnFragmentInteractionListener,
        HelpFragment.OnFragmentInteractionListener,
        FederatedFragment.OnFragmentInteractionListener,           //añado un federatedfragment
        AboutFragment.OnFragmentInteractionListener,
        PrivacySettingsFragment.OnFragmentInteractionListener,
        ContributeSettingsFragment.OnFragmentInteractionListener,
        GSMainAboutFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        AntMonitorActivity {

    private static final String TAG = "AntMonitorActivity";
    NotificationManager manager;
    Notification myNotication;
    // See ANTMONITOR-98
    public static final boolean ENABLE_CONTRIBUTION_SECTIONS = true;

    public static final String INTENT_START_ANTMONITOR = "INTENT_START_ANTMONITOR";

    /** The controller that will be used to start/stop the VPN service */
    private VpnController mVpnController;

    /** Switch that allows users to turn the VPN service on/off */
    //private Switch antMonitorSwitch;
   public static  Switch antMonitorSwitch;       //para poder desactivarlo para el reporte y luego activarlo otra vez.

    /** Indicates whether or not we got here from Getting Started */
    private boolean initialStart;

    //
    Button Federated ;

    public static Session sess;
    public static byte[] graphDef;
    public static byte[] variableAuxCheck;
public boolean notifiacion_unaVez = true;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Uncomment if you want strictmode logs */
        /*
        if (BuildConfig.DEBUG) {
            // Activate StrictMode
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    // alternatively .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    // alternatively .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_monitor_main);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        updateAfterBackStackChange();
                    }
                });


        // Initialize the controller
        mVpnController = VpnController.getInstance(this);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);             ////////////////////////////////////////////////////////// test click notification
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // if contribution is not enabled
        if (!AntMonitorMainActivity.ENABLE_CONTRIBUTION_SECTIONS) {
            Menu menu = navigationView.getMenu();
            MenuItem contributeMenuItem = menu.findItem(R.id.nav_contribute);
            contributeMenuItem.setEnabled(AntMonitorMainActivity.ENABLE_CONTRIBUTION_SECTIONS);
            contributeMenuItem.setTitle(getResources().getString(R.string.main_title_contribute_off));
        }

        View headerView = navigationView.getHeaderView(0);

        antMonitorSwitch = (Switch) headerView.findViewById(R.id.antMonitor_switch);

        // Connect and disconnect buttons are disabled by default.
        // We update enabled state when we receive a broadcast about VPN state from the service.
        // Or when the service connection is established.
        updateAntmonitorSwitch(false, false);

        // Use click events only, for simplicity
        antMonitorSwitch.setOnClickListener(this);
        // Disable swipe
        antMonitorSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });

        TextView antMonitorIDTextView = (TextView) headerView.findViewById(R.id.antMonitorId);
        updateAntMonitorID(antMonitorIDTextView);

        // default fragment is realtimefragment
        if (savedInstanceState == null) {
            String title = getResources().getString(R.string.main_title_real_time);
            RealTimeFragment fragment = new RealTimeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            toolbar.setTitle(title);
            navigationView.setCheckedItem(R.id.nav_real_time);
        }

        // update getting started complete so we do not ever go back to getting started
        SharedPreferences sp = getSharedPreferences(PreferenceTags.PREFS_TAG, Context.MODE_PRIVATE);
        boolean gettingStartedCompleted = sp.getBoolean(PreferenceTags.PREFS_GETTING_STARTED_COMPLETE, false);
        if (!gettingStartedCompleted) {
            sp.edit().putBoolean(PreferenceTags.PREFS_GETTING_STARTED_COMPLETE, true).apply();
        }

        // Remember if we got here from Getting Started
        Intent intent = getIntent();
        initialStart = intent.getBooleanExtra(INTENT_START_ANTMONITOR, false);

        // rebuild searchStrings
        AnalysisHelper.startRebuildSearchTree(this);




        /*// sesion
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
        //////////////////////////////////
        //notifiaction test mio 05/04/2021
        createNotificationChannel();

        //test clickable
        Intent intent2 = new Intent(AntMonitorMainActivity.this, ReminderBroadcast.class);
       PendingIntent pendingIntent  = PendingIntent.getBroadcast(AntMonitorMainActivity.this, 0,intent2,0);




        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtButtonClick = System.currentTimeMillis();
      long tenSencondsInMillis = 8640000; //un día, si no entras en un dis te avisa
       // long tenSencondsInMillis = 10000; //10 segundos
        alarmManager.set(AlarmManager.RTC_WAKEUP,
               timeAtButtonClick + tenSencondsInMillis,
                pendingIntent);
        //notifiaction test mio 05/04/2021
        //////////////////////////////////

        //test clickable:
        Calendar rightNow2 = Calendar.getInstance();
        int currentHourIn24Format2= rightNow2.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

        int currentHourIn12Format2 = rightNow2.get(Calendar.HOUR); // return the hour in 12 hrs format (ranging from 0-11)
        Calendar c2 = Calendar.getInstance();
        int min = rightNow2.get(Calendar.MINUTE);

        int dayOfWeek = c2.get(Calendar.DAY_OF_WEEK);

        if ((currentHourIn24Format2 ==67)&&(notifiacion_unaVez)){
/*
            NotificationManager notificationManager = (NotificationManager) AntMonitorMainActivity.this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(icon, message, when);

            Intent notificationIntent = new Intent(AntMonitorMainActivity.this, HomeActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent3 = PendingIntent.getActivity(AntMonitorMainActivity.this, 0,
                    notificationIntent, 0);

            notification.setLatestEventInfo(context, title, message, intent3);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notification);


 */
            Notification("Enter to AntMonitor and check the latest Apps exposure Report","jlasjlfjlalfdj");

        }
        ExampleRunnable runnable = new ExampleRunnable();
         new Thread(runnable).start();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Notification(String notificationTitle,
                              String notificationMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        android.app.Notification notification = new android.app.Notification(
                android.support.compat.R.drawable.notification_icon_background, "Message from Dipak Keshariya! (Android Developer)",
                System.currentTimeMillis());

        Intent notificationIntent = new Intent(this, AntMonitorMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle);
        notification = builder.build();

        notificationManager.notify(10001, notification);

    }
    public void addNotification()
    {
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(AntMonitorMainActivity.this);
        mBuilder.setSmallIcon(android.support.compat.R.drawable.notification_icon_background);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi,This notification for you let me check");
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent conPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(conPendingIntent);

        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,mBuilder.build());
        Toast.makeText(AntMonitorMainActivity.this, "Notification", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void updateAfterBackStackChange() {
        // current fragment showing after back pressing
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (fragment != null) {
            // update title bar
            toolbar.setTitle(fragment.getTag());

            // update navigation
            // AQUI SE CAMBIA A DIFERENTES ACTIVITYS EN FUNCIÓN DE CUAL SE SELECCIONE: SI QUIERO CREAR UNA NUEVA DEBE SER AQUÍ.
            if (fragment instanceof RealTimeFragment) {
                navigationView.setCheckedItem(R.id.nav_real_time);
            } else if (fragment instanceof PrivacyLeaksReportFragment) {
                navigationView.setCheckedItem(R.id.nav_privacy);
            } else if (fragment instanceof ContributeLogsFragment) {
                navigationView.setCheckedItem(R.id.nav_contribute);
            } else if (fragment instanceof  HelpFragment) {
                navigationView.setCheckedItem(R.id.nav_help);
            } else if (fragment instanceof  AboutFragment) {
                navigationView.setCheckedItem(R.id.nav_about);
            } else if (fragment instanceof PrivacySettingsFragment) {
                navigationView.setCheckedItem(R.id.nav_privacy_settings);
            } else if (fragment instanceof  ContributeSettingsFragment) {
                navigationView.setCheckedItem(R.id.nav_contribute_settings);
            }
            //extra
         else if (fragment instanceof  FederatedFragment) {
         //   navigationView.setCheckedItem(R.id.nav_federated);
                navigationView.setCheckedItem(R.id.nav_federated);
        }




         //

            else {
                navigationView.setCheckedItem(R.id.nav_real_time);

            }

        }
    }
    public void URL_button_funcion(int i){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "https://riptutorial.com/android/example/549/open-a-url-in-a-browser"));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( PrivacyLeaksReportFragment.lista_url_button.get(i)));
        Log.i("valor url *******: ",""+PrivacyLeaksReportFragment.lista_url_button.get(i));

        startActivity(intent);
    }
    /**
     * Convenience method for starting the VPN service
     */
    private void startAntMonitor() {
        // TODO update license to remove GPLv2+, update documentation, update Gradle?

        // Check if we are connected to the internet to see if it makes sense to
        // establish the VPN connection
        if (!mVpnController.isConnectedToInternet()) {
            // Not connected to internet: inform the user and do nothing
            Toast.makeText(AntMonitorMainActivity.this, R.string.no_service,
                    Toast.LENGTH_LONG).show();

            // Set button states appropriately
            updateAntmonitorSwitch(true, false);
            return;
        }

        // Check to see if we have VPN rights from the user
        Intent intent = android.net.VpnService.prepare(AntMonitorMainActivity.this);
        if (intent != null) {
            // Ask user for VPN rights. If they are granted,
            // onActivityResult will be called with RESULT_OK
            startActivityForResult(intent, VpnController.REQUEST_VPN_RIGHTS);
        } else {
            // VPN rights were granted before, attempt a connection
            onActivityResult(VpnController.REQUEST_VPN_RIGHTS, RESULT_OK, null);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.antMonitor_switch){
            antMonitorSwitch.setEnabled(false);

            // User wants to connect:
            if (antMonitorSwitch.isChecked()) {
                startAntMonitor();
            } else {
                // User wants to disconnect
                mVpnController.disconnect();
            }
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (request == VpnController.REQUEST_VPN_RIGHTS) {
            // Check if the user granted us rights to VPN
            if (result == Activity.RESULT_OK) {
                // If so, we can attempt a connection

                // Get consumers/filters specific to AntMonitor
                PacketConsumer incConsumer = VpnStarterUtils.getIncConsumer(this);
                PacketConsumer outConsumer = VpnStarterUtils.getOutConsumer(this);
                OutPacketFilter outFilter = VpnStarterUtils.getOutFilter(this);
                IncPacketFilter incFilter = VpnStarterUtils.getIncFilter(this);
                VpnStarterUtils.setSSLBumping(this);

                mVpnController.connect(incFilter, outFilter, incConsumer, outConsumer);
            } else {
                // enable the switch again so user can try again
                antMonitorSwitch.setEnabled(true);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.vpn_rights_needed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Updates this {@code Activity} to reflect a change in the state of the VPN connection.
     * Receiving this state change means we successfully bounded to the VPN service.
     * @param vpnState The new state of the VPN connection.
     */
    @Override
    public void onVpnStateChanged(VpnState vpnState) {
        boolean isConnecting = vpnState == VpnState.CONNECTING;
        boolean isConnected = vpnState == VpnState.CONNECTED;

        // Now that we are bound, we can check if we should connect automatically -> check
        // if we got here from getting started and we need to connect based on the current state
        if (!isConnecting && !isConnected && initialStart) {
            // Disable button during connection process
            updateAntmonitorSwitch(false, false);
            startAntMonitor();

            // In future calls to this method, do not attempt to re-start automatically
            initialStart = false;
        } else {
            updateAntmonitorSwitch(!isConnecting, isConnected);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Bind to the service so we can receive VPN status updates (see onVpnStateChanged)
        mVpnController.bind();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service as we no longer need to receive VPN status updates,
        // since we don't have to change the button to enabled/disabled, etc.
        mVpnController.unbind();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ant_monitor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_option_privacy_settings) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_privacy_settings);
            if (menuItem != null) {
                this.onNavigationItemSelected(menuItem);
                navigationView.setCheckedItem(R.id.nav_privacy_settings);
                return true;
            }
        } else if (id == R.id.menu_option_contribute_settings) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_contribute_settings);
            if (menuItem != null) {
                this.onNavigationItemSelected(menuItem);
                navigationView.setCheckedItem(R.id.nav_contribute_settings);
                return true;
            }
        } else if (id == R.id.menu_option_help) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_help);
            if (menuItem != null) {
                this.onNavigationItemSelected(menuItem);
                navigationView.setCheckedItem(R.id.nav_help);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // NAVEGACION ENTRE PANTALLAS*****
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = "";
        Class fragmentKlass = null;
        if (id == R.id.nav_real_time) {
            title = getResources().getString(R.string.main_title_real_time);
            fragmentKlass = RealTimeFragment.class;
        } else if (id == R.id.nav_privacy) {
            title = getResources().getString(R.string.main_title_privacy);
            fragmentKlass = PrivacyLeaksReportFragment.class;
        } else if (id == R.id.nav_contribute) {
            title = getResources().getString(R.string.main_title_contribute);
            fragmentKlass = ContributeLogsFragment.class;
        } else if (id == R.id.nav_help) {
            title = getResources().getString(R.string.main_title_help);
            fragmentKlass = HelpFragment.class;
        } else if (id == R.id.nav_about) {
            title = getResources().getString(R.string.main_title_about);
            fragmentKlass = AboutFragment.class;
        } else if (id == R.id.nav_privacy_settings) {
            title = getResources().getString(R.string.main_title_privacy_settings);
            fragmentKlass = PrivacySettingsFragment.class;
        } else if (id == R.id.nav_contribute_settings) {
            title = getResources().getString(R.string.main_title_contribute_settings);
            fragmentKlass = ContributeSettingsFragment.class;
        }

        //federated:
        else if (id == R.id.nav_federated) {
            title = getResources().getString(R.string.main_title_federated);
            if (MasInformacionFragment.cambiar_vista) {
                fragmentKlass = MasInformacionFragment.class;
            }
            else  fragmentKlass = FederatedFragment.class;
          //  fragmentKlass = PrivacyLeaksReportFragment.class;

        }

        //
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (fragmentKlass != null && currentFragment.getClass().equals(fragmentKlass)) {
            // if current fragment is the same, then do nothing
            return true;
        }

        if (title != null) {
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(title, 0);

            // if the fragment is not in the backstack, then add it.
            if (!fragmentPopped && fragmentKlass != null && getSupportFragmentManager().findFragmentByTag(title) == null) {
                try {
                    //Log.d(TAG, "Adding to backstack: " + title);
                    Fragment fragment = null;
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragment  = (Fragment) fragmentKlass.newInstance();
                    fragmentTransaction.replace(R.id.main_frame, fragment, title);
                    fragmentTransaction.addToBackStack(title);
                    fragmentTransaction.commit();
                    toolbar.setTitle(title);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Convenience method for setting the switch state based on the VPN state
     * @param enabled
     * @param checked
     */
    private void updateAntmonitorSwitch (boolean enabled, boolean checked) {
        antMonitorSwitch.setEnabled(enabled);
        antMonitorSwitch.setChecked(checked);
    }

    private void updateAntMonitorID (TextView textView) {
        String id = Installation.id(this);
        if (id != null){
            textView.setText(id);
        } else {
            textView.setText(getResources().getString(R.string.antmonitor_id));
        }
    }

    public void onPrivacyReportsToggle(View view) {
        RadioGroup radioGroup = (RadioGroup) view.getParent();
        radioGroup.clearCheck();
        radioGroup.check(view.getId());
    }

    class ExampleRunnable implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)




        @Override
        public void run(){

            // ExampleRunnable runnable = new ExampleRunnable();
            // new Thread(runnable).start();


            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);




            Calendar rightNow = Calendar.getInstance();
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

            int currentHourIn12Format = rightNow.get(Calendar.HOUR); // return the hour in 12 hrs format (ranging from 0-11)
            Calendar c = Calendar.getInstance();
            int min = rightNow.get(Calendar.MINUTE);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


            Log.i("Output: isModelUpdated", "fuera while");

              while(true){
           /* if((currentHourIn24Format ==15)&&(notifiacion_unaVez)) {
                notifiacion_unaVez = false;
                Log.i("Output: isModelUpdated", "dentro while");

                Notification("Enter to AntMonitor and check the latest Apps exposure Report", "kasdlla");
            }

            */

            if((currentHourIn12Format ==9)&&(notifiacion_unaVez)) {
                notifiacion_unaVez = false;
                Log.i("Output: isModelUpdated", "dentro while");

                Notification("Activate the VPN slider in order to start monitoring your phone. You can activate it at any moment!", "kasdlla");
            }
                  if(currentHourIn12Format ==8) {
                      notifiacion_unaVez = true;

                  }
                 }


        }

    }
}
