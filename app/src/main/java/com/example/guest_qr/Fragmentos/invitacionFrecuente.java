package com.example.guest_qr.Fragmentos;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.guest_qr.R;
import com.example.guest_qr.menuIzquierdo;

import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;


public class invitacionFrecuente extends Fragment {
    CheckBox chBxLunes, chBxMartes, chBxMiercoles, chBxJueves, chBxViernes;
    CheckBox chBxSabado, chBxDomingo;

    EditText txtHoraDesde, txtHoraHasta;
    Button btnInvitacion;

    String lunes = "N", martes = "N", miercoles = "N", jueves = "N", viernes = "N";
    String sabado = "N", domingo = "N";

    private RequestQueue mQueue;
    private String token = "";

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Variables para obtener la hora y minutos que se permite el ingreso
    Calendar fecha = Calendar.getInstance();
    final int horaDesde = fecha.get(Calendar.HOUR_OF_DAY);
    final int minutoDesde = fecha.get(Calendar.MINUTE);
    final int horaHasta = fecha.get(Calendar.HOUR_OF_DAY);
    final int minutoHasta = fecha.get(Calendar.MINUTE);

    //Variables del fragment anterior
    String idInvitado, numeroInvitado, qr, placa, FechaDesde, FechaHasta;

    private OnFragmentInteractionListener mListener;

    public invitacionFrecuente() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invitacion_frecuente, container, false);


        mQueue = Volley.newRequestQueue(getActivity());

        //Obtiene el token de la activity
        menuIzquierdo activity = (menuIzquierdo) getActivity();
        token = activity.getToken();

        //Referencia de los controles
        txtHoraDesde = v.findViewById(R.id.txtHoraDesde);
        txtHoraHasta = v.findViewById(R.id.txtHoraHasta);

        chBxLunes = v.findViewById(R.id.chBxLunes);
        chBxMartes = v.findViewById(R.id.chBxMartes);
        chBxMiercoles = v.findViewById(R.id.chBxMiercoles);
        chBxJueves = v.findViewById(R.id.chBxJueves);
        chBxViernes = v.findViewById(R.id.chBxViernes);
        chBxSabado = v.findViewById(R.id.chBxSabado);
        chBxDomingo = v.findViewById(R.id.chBxDomingo);

        btnInvitacion = v.findViewById(R.id.btnInvitacion);

        //Extraer valores de la pantalla anterior
        Bundle bundle = this.getArguments();
        qr = bundle.getString("qr");
        idInvitado = bundle.getString("idInvitado");
        numeroInvitado = bundle.getString("numInvitado");
        placa = bundle.getString("placa_vehiculo");
        FechaDesde = bundle.getString("fecha_desde");
        FechaHasta = bundle.getString("fecha_hasta");
        System.out.println(FechaHasta);

        btnInvitacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearFrecuencia();

            }
        });

        //Hora Desde
        txtHoraDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHoraDesde();
            }
        });

        //Hora Hasta
        txtHoraHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHoraHasta();
            }
        });

        //Control del checkbox para lunes
        chBxLunes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lunes = "S";

                } else {
                    lunes = "N";
                }
            }
        });


        //Control del checkbox para martes
        chBxMartes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    martes = "S";

                } else {
                    martes = "N";
                }
            }
        });


        //Control del checkbox para miercoles
        chBxMiercoles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    miercoles = "S";

                } else {
                    miercoles = "N";
                }
            }
        });


        //Control del checkbox para jueves
        chBxJueves.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    jueves = "S";

                } else {
                    jueves = "N";
                }
            }
        });


        //Control del checkbox para viernes
        chBxViernes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viernes = "S";

                } else {
                    viernes = "N";
                }
            }
        });


        //Control del checkbox para sabado
        chBxSabado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sabado = "S";

                } else {
                    sabado = "N";
                }
            }
        });


        //Control del checkbox para domingo
        chBxDomingo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    domingo = "S";

                } else {
                    domingo = "N";
                }
            }
        });

        return v;
    }

    //Obtener hora desde
    private void obtenerHoraDesde(){
        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? valueOf(CERO + hourOfDay) : valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? valueOf(CERO + minute): valueOf(minute);

                //Muestro la hora con el formato deseado
                txtHoraDesde.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, horaDesde, minutoDesde, false);

        recogerHora.show();
    }


    //Obtener hora hasta
    private void obtenerHoraHasta(){
        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? valueOf(CERO + hourOfDay) : valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? valueOf(CERO + minute): valueOf(minute);

                //Muestro la hora con el formato deseado
                txtHoraHasta.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, horaHasta, minutoHasta, false);

        recogerHora.show();
    }

    public void crearFrecuencia(){
        Map<String, String> params = new HashMap();

        System.out.println(txtHoraHasta.getText().toString());
        System.out.println("Lunes:");
        params.put("hora_desde", txtHoraDesde.getText().toString());
        params.put("hora_hasta", txtHoraHasta.getText().toString());
        params.put("lunes", lunes);
        params.put("martes", martes);
        params.put("miercoles", miercoles);
        params.put("jueves", jueves);
        params.put("viernes", viernes);
        params.put("sabado", sabado);
        params.put("domingo", domingo);

        JSONObject parametros = new JSONObject(params);

        String url = "http://52.67.115.36/api/frecuencia";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        try {
                            int frecuencia = response.getInt("frecuencia");

                            crearInvitacion(frecuencia);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagjsonexp", "" + error.toString());

                System.out.println("Error: "+error);
                AlertDialog alertDialog = new
                        AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al crear la frecuencia");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int
                                    which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }

    //genera la invitación
    public void crearInvitacion(int frecuencia){
        Map<String, String> params = new HashMap();
        System.out.println(FechaDesde);

        params.put("serial", qr);
        params.put("placa_vehiculo", placa);
        params.put("fecha_desde", FechaDesde);
        params.put("fecha_hasta", FechaHasta);
        params.put("state", "A");
        params.put("frecuencia", valueOf(frecuencia));

        JSONObject parametros = new JSONObject(params);

        String url = "http://52.67.115.36/api/nuevainvitacion/"+idInvitado;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        codigoQR fragment = new codigoQR();
                        Bundle bundle = new Bundle();
                        bundle.putString("qr", qr);
                        bundle.putString("numInvitado", numeroInvitado);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_menu_izquierdo,fragment)
                                .addToBackStack(null)
                                .commit();

                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new
                        AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al crear la invitación");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int
                                    which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
