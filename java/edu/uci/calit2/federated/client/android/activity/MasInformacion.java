package edu.uci.calit2.federated.client.android.activity;

//import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.List;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */

public class MasInformacion extends AppCompatActivity {
    public final static String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
    public static final String ERROR = "error";

    TextView titulo;
    TextView subtitulo;
    TextView tercertitulo;
    TextView location_text;
    TextView email_text;
    TextView device_text;
    TextView imei_text;
    TextView serialnumber_text;
    TextView macaddress_text;
    TextView advertiser_text;

    Button button ;
    Button volver ;
  /*
    EditText location_input;
    EditText email_input;
    EditText device_input;
    EditText imei_input;
    EditText serialnumber_input;
    EditText macaddress_input;
    EditText advertiser_input;


   */
    SeekBar location_input;
    SeekBar email_input;
    SeekBar device_input;
    SeekBar barra_location;
    SeekBar imei_input;
    SeekBar serialnumber_input;
    SeekBar macaddress_input;
    SeekBar advertiser_input;

    // pesos a cambiar:
    float Plocation ;
    float Pemail ;
    float Pimei ;
    float Pdevice ;
    float Pserialnumber ;
    float Pmacaddress ;
    float Padvertiser ;

    boolean actualizacion_datos_activity = false;



    //pasar variables a otra actividad:
    public static final String PLOCATION = "Plocation";
    public static final String PEMAIL = "Pemail";
    public static final String PDEVICE = "Pdevice";
    public static final String PIMEI = "Pimei";
    public static final String PSERIALNUMBER = "Pserialnumber";
    public static final String PMACADDRESS = "Pmacaddresss";
    public static final String PADVERTISER = "Padvertiser";

    public static final String ACTUALIZACION_DATOS_ACTIVITY = "actualizacion_datos_activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_informacion);

        titulo = (TextView) findViewById(R.id.titulo);
        subtitulo = (TextView) findViewById(R.id.subtitulo);
        tercertitulo = (TextView) findViewById(R.id.tercertitulo);
        location_text = (TextView) findViewById(R.id.location_text);
        email_text = (TextView) findViewById(R.id.email_text);
        device_text = (TextView) findViewById(R.id.device_text);
        imei_text = (TextView) findViewById(R.id.imei_text);
        serialnumber_text = (TextView) findViewById(R.id.serialnumber_text);
        macaddress_text = (TextView) findViewById(R.id.macaddress_text);
        advertiser_text = (TextView) findViewById(R.id.advertiser_text);
        //
        /*
        location_input = (EditText) findViewById(R.id.location_input);
        email_input = (EditText) findViewById(R.id.email_input);
        device_input = (EditText) findViewById(R.id.device_input);
        imei_input = (EditText) findViewById(R.id.imei_input);
        serialnumber_input = (EditText) findViewById(R.id.serialnumber_input);
        macaddress_input = (EditText) findViewById(R.id.macaddress_input);
        advertiser_input = (EditText) findViewById(R.id.advertiser_input);


         */
        button = (Button) findViewById(R.id.button);
        volver = (Button) findViewById(R.id.volver);

      //  barra_location = (SeekBar) findViewById(R.id.barra_location);
        location_input = (SeekBar) findViewById(R.id.location_input);
        email_input = (SeekBar) findViewById(R.id.email_input);
        device_input = (SeekBar) findViewById(R.id.device_input);
        imei_input = (SeekBar) findViewById(R.id.imei_input);
        serialnumber_input = (SeekBar) findViewById(R.id.serialnumber_input);
        macaddress_input = (SeekBar) findViewById(R.id.macaddress_input);
        advertiser_input = (SeekBar) findViewById(R.id.advertiser_input);

        //recupero datos:

        Intent intent = getIntent();
        Plocation = intent.getFloatExtra(MainActivity.PLOCATION, 0);
        Pemail = intent.getFloatExtra(MainActivity.PEMAIL, 0);
        Pdevice = intent.getFloatExtra(MainActivity.PDEVICE, 0);
        Pimei = intent.getFloatExtra(MainActivity.PIMEI, 0);
        Pserialnumber = intent.getFloatExtra(MainActivity.PSERIALNUMBER, 0);
        Pmacaddress = intent.getFloatExtra(MainActivity.PMACADDRESS, 0);
        Padvertiser = intent.getFloatExtra(MainActivity.PADVERTISER, 0);




        location_text.setText("-Location Weight = "+(int) Plocation);
        email_text.setText("-Email Weight = "+(int) Pemail);
        device_text.setText("-Device ID Weight = "+(int) Pdevice);
        imei_text.setText("-Imei Weight = "+(int) Pimei);
        serialnumber_text.setText("-Serial Number Weight = "+(int) Pserialnumber);
        macaddress_text.setText("-Mac Address Weight = "+(int) Pmacaddress);
        advertiser_text.setText("-Advertiser ID Weight = "+(int) Padvertiser);


/// barras progreso:
        location_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Plocation = (float) ((float) progress*0.1);
                location_text.setText("-Location Weight = "+(int) Plocation);
                if (location_text.getText() == "0"){
                    location_text.setText("-Location Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        location_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Plocation = (float) ((float) progress*0.1);
                location_text.setText("-Location Weight = "+(int) Plocation);
                if (location_text.getText() == "0"){
                    location_text.setText("-Location Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        email_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pemail = (float) ((float) progress*0.1);
                email_text.setText("-Email Weight = "+(int) Pemail);
                if (email_text.getText() == "0"){
                    email_text.setText("-Email Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        device_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pdevice = (float) ((float) progress*0.1);
                device_text.setText("-Device ID Weight = "+(int) Pdevice);
                if (device_text.getText() == "0"){
                    device_text.setText("-Device ID Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imei_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pimei = (float) ((float) progress*0.1);
                imei_text.setText("-IMEI Weight = "+(int) Pimei);
                if (imei_text.getText() == "0"){
                    imei_text.setText("-IMEI Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        serialnumber_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pserialnumber = (float) ((float) progress*0.1);
                serialnumber_text.setText("-Serial Number Weight = "+(int) Pserialnumber);
                if (serialnumber_text.getText() == "0"){
                    serialnumber_text.setText("-Serial Number Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        macaddress_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Pmacaddress = (float) ((float) progress*0.1);
                macaddress_text.setText("-Mac Address Weight = "+(int) Pmacaddress);
                if (macaddress_text.getText() == "0"){
                    macaddress_text.setText("-Mac Address Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        advertiser_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Padvertiser = (float) ((float) progress*0.1);
                advertiser_text.setText("-Advertiser ID Weight = "+(int) Padvertiser);
                if (advertiser_text.getText() == "0"){
                    advertiser_text.setText("-Advertiser ID Weight = "+1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //BOTON GUARDAR
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                Plocation = (float)(location_input.getProgress()*0.1);
                if (location_text.getText() == "0"){
                    Plocation = 1;}

                    Pemail = (float)(email_input.getProgress()*0.1);
                    if (email_text.getText() == "0"){
                        Pemail = 1;}


                        Pdevice = (float)(device_input.getProgress()*0.1);
                        if (device_text.getText() == "0"){
                            Pdevice = 1;}

                            Pimei = (float)(imei_input.getProgress()*0.1);
                            if (imei_text.getText() == "0"){
                                Pimei = 1;}

                                Pserialnumber = (float)(serialnumber_input.getProgress()*0.1);
                                if (serialnumber_text.getText() == "0"){
                                    Pserialnumber = 1;}

                                    Pmacaddress = (float)(macaddress_input.getProgress()*0.1);
                                    if (macaddress_text.getText() == "0"){
                                        Pmacaddress = 1;}

                                        Padvertiser = (float)(advertiser_input.getProgress()*0.1);
                                        if (advertiser_text.getText() == "0"){
                                            Padvertiser = 1;}
////////////////////////////////////////
                PrivacyLeaksReportFragment.Plocation = (float)(location_input.getProgress()*0.1);
                if (location_text.getText() == "0"){
                    PrivacyLeaksReportFragment.Plocation = 1;}

                PrivacyLeaksReportFragment.Pemail = (float)(email_input.getProgress()*0.1);
                    if (email_text.getText() == "0"){
                        PrivacyLeaksReportFragment.Pemail = 1;}


                PrivacyLeaksReportFragment.Pdevice = (float)(device_input.getProgress()*0.1);
                        if (device_text.getText() == "0"){
                            PrivacyLeaksReportFragment.Pdevice = 1;}

                PrivacyLeaksReportFragment.Pimei = (float)(imei_input.getProgress()*0.1);
                            if (imei_text.getText() == "0"){
                                PrivacyLeaksReportFragment.Pimei = 1;}

                PrivacyLeaksReportFragment.Pserialnumber = (float)(serialnumber_input.getProgress()*0.1);
                                if (serialnumber_text.getText() == "0"){
                                    PrivacyLeaksReportFragment.Pserialnumber = 1;}

                PrivacyLeaksReportFragment.Pmacaddress = (float)(macaddress_input.getProgress()*0.1);
                                    if (macaddress_text.getText() == "0"){
                                        PrivacyLeaksReportFragment.Pmacaddress = 1;}

                PrivacyLeaksReportFragment.Padvertiser = (float)(advertiser_input.getProgress()*0.1);
                                        if (advertiser_text.getText() == "0"){
                                            PrivacyLeaksReportFragment.Padvertiser = 1;}

                                        /*
                    Pemail = Float.parseFloat(email_input.getText().toString());

                Pdevice = Float.parseFloat(device_input.getText().toString());

                Pimei = Float.parseFloat(imei_input.getText().toString());

                Pserialnumber = Float.parseFloat(serialnumber_input.getText().toString());

                Pmacaddress = Float.parseFloat(macaddress_input.getText().toString());

                Padvertiser = Float.parseFloat(advertiser_input.getText().toString());
////////////////////////////////////////
                FederatedFragment.Plocation = Float.parseFloat(location_input.getText().toString());

                FederatedFragment.Pemail = Float.parseFloat(email_input.getText().toString());

                FederatedFragment.Pdevice = Float.parseFloat(device_input.getText().toString());

                FederatedFragment.Pimei = Float.parseFloat(imei_input.getText().toString());

                FederatedFragment.Pserialnumber = Float.parseFloat(serialnumber_input.getText().toString());

                FederatedFragment.Pmacaddress = Float.parseFloat(macaddress_input.getText().toString());

                FederatedFragment.Padvertiser = Float.parseFloat(advertiser_input.getText().toString());




/*
                location_text.setText("-Peso Location = "+(int) Plocation);
                email_text.setText("-Peso Email = "+(int) Pemail);
                device_text.setText("-Peso Device = "+(int) Pdevice);
                imei_text.setText("-Peso Imei = "+(int) Pimei);

 */
                location_text.setText("-Location Weight = "+(int) Plocation);
                email_text.setText("-Email Weight = "+(int) Pemail);
                device_text.setText("-Device ID Weight = "+(int) Pdevice);
                imei_text.setText("-Imei Weight = "+(int) Pimei);
                serialnumber_text.setText("-Serial Number Weight = "+(int) Pserialnumber);
                macaddress_text.setText("-Mac Address Weight = "+(int) Pmacaddress);
                advertiser_text.setText("-Advertiser ID Weight = "+(int) Padvertiser);

                Toast.makeText(getApplicationContext(), "Weights changed", Toast.LENGTH_SHORT).show();


            }
        });


        //BOTON VOLVER 
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //  startActivity(new Intent(MainActivity.this, MasInformacion.class));
                Intent intent = new Intent(MasInformacion.this, AntMonitorMainActivity.class);
                intent.putExtra(PLOCATION, Plocation);
                intent.putExtra(PEMAIL, Pemail);
                intent.putExtra(PDEVICE, Pdevice);
                intent.putExtra(PIMEI, Pimei);
                intent.putExtra(PSERIALNUMBER, Pserialnumber);
                intent.putExtra(PMACADDRESS, Pmacaddress);
                intent.putExtra(PADVERTISER, Padvertiser);

                actualizacion_datos_activity = true;
                intent.putExtra(ACTUALIZACION_DATOS_ACTIVITY, actualizacion_datos_activity);
                startActivity(intent);







            }
        });


    }
    public  class FetchCategoryTask extends AsyncTask<Void, Void, Void> {

        private final String TAG = FetchCategoryTask.class.getSimpleName();
        private PackageManager pm;
        //private ActivityUtil mActivityUtil;

        @Override
        protected Void doInBackground(Void... errors) {
            String category;
            pm = getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            Iterator<ApplicationInfo> iterator = packages.iterator();
            //  while (iterator.hasNext()) {
            // ApplicationInfo packageInfo = iterator.next();
            String query_url = "https://play.google.com/store/apps/details?id=com.imo.android.imoim";  //GOOGLE_URL + packageInfo.packageName;
            Log.i(TAG, query_url);
            category = getCategory(query_url);
            Log.e("CATEGORY", category);

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
}







