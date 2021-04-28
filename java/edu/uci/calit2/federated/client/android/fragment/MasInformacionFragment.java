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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.uci.calit2.federated.R;
import edu.uci.calit2.federated.client.android.activity.MainActivity;

//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.gms.common.util.ArrayUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasInformacionFragment extends Fragment {


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
    EditText location_input;
    EditText email_input;
    EditText device_input;
    EditText imei_input;
    EditText serialnumber_input;
    EditText macaddress_input;
    EditText advertiser_input;

    // pesos a cambiar:
    float Plocation = 0;
    float Pemail = 0;
    float Pimei = 0;
    float Pdevice = 0;
    float Pserialnumber = 0;
    float Pmacaddress = 0;
    float Padvertiser = 0;

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

    public static boolean cambiar_vista;


    //  private HelpFragment.OnFragmentInteractionListener mListener;
  private MasInformacionFragment.OnFragmentInteractionListener mListener;
//comento
    /*
    public HelpFragment() {
        // Required empty public constructor
    }

     */
public MasInformacionFragment() {
    // Required empty public constructor
}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HelpFragment.
     */
    public static MasInformacionFragment newInstance(String param1, String param2) {
        MasInformacionFragment fragment = new MasInformacionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


//antiguo:
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_federated, container, false);          /// OJO AQUI EL LAYOUT:

        TextView learnMore = (TextView) view.findViewById(R.id.learn_more);
        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent learnMoreLink = new Intent(Intent.ACTION_VIEW);
                learnMoreLink.addCategory(Intent.CATEGORY_BROWSABLE);
                learnMoreLink.setData(Uri.parse(getResources().getString(R.string.getting_started_learn_more_url)));
                startActivity(learnMoreLink);
            }
        });



        return view;
    }

     */
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {


    View view = inflater.inflate(R.layout.activity_mas_informacion, container, false);




///////////////////////////
    titulo = (TextView) view.findViewById(R.id.titulo);
    subtitulo = (TextView) view.findViewById(R.id.subtitulo);
    tercertitulo = (TextView) view.findViewById(R.id.tercertitulo);
    location_text = (TextView) view.findViewById(R.id.location_text);
    email_text = (TextView) view.findViewById(R.id.email_text);
    device_text = (TextView) view.findViewById(R.id.device_text);
    imei_text = (TextView) view.findViewById(R.id.imei_text);
    serialnumber_text = (TextView) view.findViewById(R.id.serialnumber_text);
    macaddress_text = (TextView) view.findViewById(R.id.macaddress_text);
    advertiser_text = (TextView) view.findViewById(R.id.advertiser_text);
    //
    location_input = (EditText) view.findViewById(R.id.location_input);
    email_input = (EditText) view.findViewById(R.id.email_input);
    device_input = (EditText) view.findViewById(R.id.device_input);
    imei_input = (EditText) view.findViewById(R.id.imei_input);
    serialnumber_input = (EditText) view.findViewById(R.id.serialnumber_input);
    macaddress_input = (EditText) view.findViewById(R.id.macaddress_input);
    advertiser_input = (EditText) view.findViewById(R.id.advertiser_input);

    button = (Button) view.findViewById(R.id.button);
    volver = (Button) view.findViewById(R.id.volver);

    //recupero datos:

    Intent intent = getActivity().getIntent();
    Plocation = intent.getFloatExtra(MainActivity.PLOCATION, 0);
    Pemail = intent.getFloatExtra(MainActivity.PEMAIL, 0);
    Pdevice = intent.getFloatExtra(MainActivity.PDEVICE, 0);
    Pimei = intent.getFloatExtra(MainActivity.PIMEI, 0);
    Pserialnumber = intent.getFloatExtra(MainActivity.PSERIALNUMBER, 0);
    Pmacaddress = intent.getFloatExtra(MainActivity.PMACADDRESS, 0);
    Padvertiser = intent.getFloatExtra(MainActivity.PADVERTISER, 0);




    location_text.setText("-Peso Location = "+(int) Plocation);
    email_text.setText("-Peso Email = "+(int) Pemail);
    device_text.setText("-Peso Device ID = "+(int) Pdevice);
    imei_text.setText("-Peso Imei = "+(int) Pimei);
    serialnumber_text.setText("-Peso Serial Number = "+(int) Pserialnumber);
    macaddress_text.setText("-Peso Mac Address = "+(int) Pmacaddress);
    advertiser_text.setText("-Peso Advertiser ID= "+(int) Padvertiser);


    //BOTON GUARDAR
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {



            Plocation = Float.parseFloat(location_input.getText().toString());

            Pemail = Float.parseFloat(email_input.getText().toString());

            Pdevice = Float.parseFloat(device_input.getText().toString());

            Pimei = Float.parseFloat(imei_input.getText().toString());

            Pserialnumber = Float.parseFloat(serialnumber_input.getText().toString());

            Pmacaddress = Float.parseFloat(macaddress_input.getText().toString());

            Padvertiser = Float.parseFloat(advertiser_input.getText().toString());


/*
                location_text.setText("-Peso Location = "+(int) Plocation);
                email_text.setText("-Peso Email = "+(int) Pemail);
                device_text.setText("-Peso Device = "+(int) Pdevice);
                imei_text.setText("-Peso Imei = "+(int) Pimei);

 */
            location_text.setText("-Peso Location = "+(int) Plocation);
            email_text.setText("-Peso Email = "+(int) Pemail);
            device_text.setText("-Peso Device ID = "+(int) Pdevice);
            imei_text.setText("-Peso Imei = "+(int) Pimei);
            serialnumber_text.setText("-Peso Serial Number = "+(int) Pserialnumber);
            macaddress_text.setText("-Peso Mac Address = "+(int) Pmacaddress);
            advertiser_text.setText("-Peso Advertiser ID= "+(int) Padvertiser);

            Toast.makeText(getActivity().getApplicationContext(), "Pesos cambiados correctamente", Toast.LENGTH_SHORT).show();

        }
    });

    //BOTON VOLVER
    volver.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            //  startActivity(new Intent(MainActivity.this, MasInformacion.class));

            cambiar_vista = false;
/*
            Intent intent = new Intent(getActivity(), FederatedFragment.class);
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

 */







        }
    });



    //////////////////////////////
    return view;
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if (context instanceof MasInformacionFragment.OnFragmentInteractionListener) {
            mListener = (MasInformacionFragment.OnFragmentInteractionListener) context;
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
