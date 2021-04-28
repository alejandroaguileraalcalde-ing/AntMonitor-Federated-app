package edu.uci.calit2.federated.client.android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.fragment.PrivacyLeaksReportFragment;

import java.util.ArrayList;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */

public class Adaptador extends BaseAdapter {
    public static final String AUX_BUTTON = "aux_button";
    public static final String CLICK_BIEN = "click_bien";
    public static final String CLICK_MAL = "click_mal";

    boolean aux_button = false;
    boolean click_bien = false;
    boolean click_mal = false;

    private ArrayList<Entidad> listItems;
    private Context context;


    public Adaptador(Context context, ArrayList<Entidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // OBTENER EL OBJETO POR CADA ITEM A MOSTRAR
       final Entidad Item = (Entidad) getItem(position);


        convertView = LayoutInflater.from(context).inflate(R.layout.item_app, null);

        TextView subtitulo = (TextView) convertView.findViewById(R.id.subtitulo);
        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView nombre_app = (TextView) convertView.findViewById(R.id.nombreapp);
      final  TextView resumen_app = (TextView) convertView.findViewById(R.id.resumen_app);

        TextView bien = (Button) convertView.findViewById(R.id.bien);
        TextView mal = (Button) convertView.findViewById(R.id.mal);
        TextView button_url = (Button) convertView.findViewById(R.id.button_url);

        TextView recomendacion = (TextView) convertView.findViewById(R.id.recomendacion);
        ImageView foto_app = (ImageView) convertView.findViewById(R.id.foto_app);
        // LLENAMOS LOS ELEMENTOS CON LOS VALORES DE CADA ITEM

        titulo.setText(Item.getValor_nivel());
        subtitulo.setText(Item.getTitulo_nivel());
        nombre_app.setText(Item.getNombre_app());
        bien.setText(Item.getBien());
        mal.setText(Item.getMal());
        resumen_app.setText(Item.getResumen());
        recomendacion.setText(Item.getRecomendacion());
        //imagen

            foto_app.setImageDrawable(Item.getFoto());

        //
        if (Item.getColor() ==0){
            resumen_app.setBackgroundResource(R.drawable.stilo_borde_textview);
       //     recomendacion.setVisibility(convertView.GONE);
        }
        if (Item.getColor() ==1){
            resumen_app.setBackgroundResource(R.drawable.stilo_borde_naranja);
            //recomendacion.setVisibility(convertView.GONE);
        }
        if (Item.getColor() ==2){
            resumen_app.setBackgroundResource(R.drawable.stilo_borde_rojo);
            //recomendacion.setVisibility(convertView.VISIBLE);
        }
        bien.setTag(position);


        bien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


/*
                MainActivity.aux_button2 = true;
                MainActivity.click_bien2 = true;

                MainActivity.posicion_button = position;
                MainActivity.funcionBien_static(Item.getLocation(), Item.getEmail(), Item.getDevice(), Item.getImei(),Item.getSerialnumber(),Item.getMacaddresss(),Item.getAdvertiser(), context);


 */
                PrivacyLeaksReportFragment.aux_button2 = true;
                PrivacyLeaksReportFragment.click_bien2 = true;

                PrivacyLeaksReportFragment.posicion_button = position;
                PrivacyLeaksReportFragment.funcionBien_static(Item.getLocation(), Item.getEmail(), Item.getDevice(), Item.getImei(),Item.getSerialnumber(),Item.getMacaddresss(),Item.getAdvertiser(), context, Item.getInternal_dst(), Item.getAds_dst(), Item.getSns_dst(), Item.getAnalytics_dst(), Item.getDevelop_dst());

            }
        });

        mal.setTag(position);

        mal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onClick(View v){
                /*
                MainActivity.aux_button2 = true;
                MainActivity.click_mal2 = true;

                MainActivity.posicion_button = position;
                MainActivity.funcionMal_static(Item.getLocation(), Item.getEmail(), Item.getDevice(), Item.getImei(),Item.getSerialnumber(),Item.getMacaddresss(),Item.getAdvertiser(), context);


                 */
                PrivacyLeaksReportFragment.aux_button2 = true;
                PrivacyLeaksReportFragment.click_mal2 = true;
                PrivacyLeaksReportFragment.posicion_button = position;
                PrivacyLeaksReportFragment.funcionMal_static(Item.getLocation(), Item.getEmail(), Item.getDevice(), Item.getImei(),Item.getSerialnumber(),Item.getMacaddresss(),Item.getAdvertiser(), context, Item.getInternal_dst(), Item.getAds_dst(), Item.getSns_dst(), Item.getAnalytics_dst(), Item.getDevelop_dst());
                PrivacyLeaksReportFragment.numeroAppsDisagree++;
                PrivacyLeaksReportFragment.buton_guardar.callOnClick();

            }
        });

        button_url.setTag(position);

        button_url.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onClick(View v){
                try {
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "https://riptutorial.com/android/example/549/open-a-url-in-a-browser"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + PrivacyLeaksReportFragment.lista_url_button.get(position)));
                    Log.i("valor url *******: ", "" + PrivacyLeaksReportFragment.lista_url_button.get(position));

                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "url not found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });




        return convertView;
    }
}