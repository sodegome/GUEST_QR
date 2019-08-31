package com.example.guest_qr.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.*;
import android.graphics.Color;
import java.text.DateFormat;
import java.util.Date;

import org.json.*;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;

import com.example.guest_qr.Clases.Invitados;
import com.example.guest_qr.GenerarQR;
import com.example.guest_qr.R;
import com.example.guest_qr.menuIzquierdo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class invitacionIndividual extends Fragment implements AdapterView.OnItemSelectedListener{
    Spinner spnInvitados;
    EditText txtFechaDesde, txtFechaHasta, txtHoraDesde, txtHoraHasta, txtPlaca;
    TextView txtInicio;
    Button btnRegistro;
    CheckBox chBxFrecuente;

    String serialQR;
    boolean frecuente = false;

    private RequestQueue mQueue;
    private String token = "";

    String[] strInvitados;
    String[] strIdInvitados;
    String[] strNumInvitados;
    List<String> listaInvitados;
    ArrayAdapter<String> comboAdapter;
    String invitadoSeleccionado, idInvitado, numeroInvitado;

    //Instancias de calendarios
    Calendar fechaDesde = Calendar.getInstance();
    Calendar fechaHasta = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Variables para obtener la hora y minutos que se permite el ingreso
    final int horaDesde = fechaDesde.get(Calendar.HOUR_OF_DAY);
    final int minutoDesde = fechaDesde.get(Calendar.MINUTE);
    final int horaHasta = fechaHasta.get(Calendar.HOUR_OF_DAY);
    final int minutoHasta = fechaHasta.get(Calendar.MINUTE);

    private OnFragmentInteractionListener mListener;

    public invitacionIndividual() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Invitación");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invitacion_individual, container, false);

        //Referencia de los controles
        txtFechaDesde = v.findViewById(R.id.txtFechaDesde);
        txtFechaHasta = v.findViewById(R.id.txtFechaHasta);
        txtHoraDesde = v.findViewById(R.id.txtHoraDesde);
        txtHoraHasta = v. findViewById(R.id.txtHoraHasta);

        mQueue = Volley.newRequestQueue(getActivity());

        //Obtiene el token de la activity
        menuIzquierdo activity = (menuIzquierdo) getActivity();
        token = activity.getToken();

        //Referencia a los controles
        txtInicio = v.findViewById(R.id.txtInicio);
        chBxFrecuente = v.findViewById(R.id.chBxFrecuente);
        txtPlaca = v.findViewById(R.id.txtPlaca);
        btnRegistro = v.findViewById(R.id.btnRegistro);

        //Referencia y llenar el spinner con los invitados
        spnInvitados = v.findViewById(R.id.spnInvitados);
        spnInvitados.setOnItemSelectedListener(this);
        obtenerInvitados();

        //SerialQR
        serialQR = generateString();

        //Control del checkbox para usuario frecuente
        chBxFrecuente.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    frecuente = true;
                    txtHoraDesde.setFocusable(false);
                    txtHoraDesde.setEnabled(false);
                    txtHoraHasta.setFocusable(false);
                    txtHoraHasta.setEnabled(false);

                    txtHoraDesde.getBackground().setAlpha(10);
                    txtHoraDesde.setHintTextColor(Color.parseColor("#F0F5F5"));
                    txtHoraHasta.getBackground().setAlpha(10);
                    txtHoraHasta.setHintTextColor(Color.parseColor("#F0F5F5"));

                    btnRegistro.setText("Siguiente ");
                } else {
                    frecuente = false;
                    txtHoraDesde.setFocusable(true);
                    txtHoraDesde.setEnabled(true);
                    txtHoraHasta.setFocusable(true);
                    txtHoraHasta.setEnabled(true);

                    txtHoraDesde.getBackground().setAlpha(100);
                    txtHoraDesde.setHintTextColor(Color.parseColor("#BCB7AD"));
                    txtHoraHasta.getBackground().setAlpha(100);
                    txtHoraHasta.setHintTextColor(Color.parseColor("#BCB7AD"));

                    btnRegistro.setText("Generar invitación");

                }
            }
        });


        //Llamada a generar QR
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Invitado seleccionado: "+invitadoSeleccionado);
                System.out.println("id invitado : "+idInvitado);

                String fechaDesde = formatoFechaDesde() + " " + validarHora(txtHoraDesde.getText().toString(), "I");
                String fechaHasta = formatoFechaHasta() + " " + validarHora(txtHoraDesde.getText().toString(), "F");

                if (frecuente == false){
                    System.out.println("Fecha desde: "+fechaDesde);
                    System.out.println("Fecha hasta: "+fechaHasta);
                    crearInvitacion(idInvitado, numeroInvitado, fechaDesde, fechaHasta);

                }
                else{
                    invitacionFrecuente fragment = new invitacionFrecuente();
                    Bundle bundle = new Bundle();
                    bundle.putString("qr", generateString());
                    bundle.putString("idInvitado", idInvitado);
                    bundle.putString("numInvitado", numeroInvitado);
                    bundle.putString("placa_vehiculo", txtPlaca.getText().toString());
                    bundle.putString("fecha_desde", fechaDesde);
                    bundle.putString("fecha_hasta", fechaHasta);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_menu_izquierdo,fragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        //Fecha Desde
        txtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), fDesde, fechaDesde
                        .get(Calendar.YEAR), fechaDesde.get(Calendar.MONTH),
                        fechaDesde.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Fecha Hasta
        txtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), fHasta, fechaHasta
                        .get(Calendar.YEAR), fechaHasta.get(Calendar.MONTH),
                        fechaHasta.get(Calendar.DAY_OF_MONTH)).show();
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

        return v;
    }


    //Setear los valores de la fecha desde
    DatePickerDialog.OnDateSetListener fDesde = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            fechaDesde.set(Calendar.YEAR, year);
            fechaDesde.set(Calendar.MONTH, monthOfYear);
            fechaDesde.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarFechaDesde();
        }

    };

    private void actualizarFechaDesde() {
        String formatoDeFecha = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        txtFechaDesde.setText(sdf.format(fechaDesde.getTime()));
    }

    private String formatoFechaDesde() {
        String formatoDeFecha = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        return sdf.format(fechaDesde.getTime());
    }

    //Setear los valores de la fecha hasta
    DatePickerDialog.OnDateSetListener fHasta = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            fechaHasta.set(Calendar.YEAR, year);
            fechaHasta.set(Calendar.MONTH, monthOfYear);
            fechaHasta.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarFechaHasta();
        }

    };

    private void actualizarFechaHasta() {
        String formatoDeFecha = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        txtFechaHasta.setText(sdf.format(fechaHasta.getTime()));
    }

    private String formatoFechaHasta() {
        String formatoDeFecha = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        return sdf.format(fechaHasta.getTime());
    }



    //Obtener hora desde
    private void obtenerHoraDesde(){
        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

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
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

                //Muestro la hora con el formato deseado
                txtHoraHasta.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, horaHasta, minutoHasta, false);

        recogerHora.show();
    }

    //Obtiene la lista de los invitados y llena el spinner
    public void obtenerInvitados(){
        String url = "http://52.67.115.36/api/invitados";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        strInvitados = new String[response.length()];
                        strIdInvitados = new String[response.length()];
                        strNumInvitados = new String[response.length()];
                        listaInvitados = new ArrayList<>();

                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String invitado = jsonObject.getString("name") +" "+ jsonObject.getString("last_name");
                                strInvitados[i] = invitado;
                                strIdInvitados[i] = jsonObject.getString("id");
                                System.out.println("Invitado: "+invitado);
                                strNumInvitados[i] = jsonObject.getString("cell");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.addAll(listaInvitados, strInvitados);
                        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listaInvitados);
                        //Cargo el spinner con los datos
                        spnInvitados.setAdapter(comboAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }


    //Seleccionar el invitado
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnInvitados:
                //Almaceno el invitado seleccionado
                invitadoSeleccionado = strInvitados[position];
                idInvitado = strIdInvitados[position];
                numeroInvitado = strNumInvitados[position];
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //Generar el serial para la invitacion
    public String generateString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0,6);
        return uuid.replace("-","");

    }


    //genera la invitación
    public void crearInvitacion(String idInvitado, final String numeroInvitado, String FechaDesde, String FechaHasta){
        Map<String, String> params = new HashMap();

        final String codigoQR = generateString();
        params.put("serial", codigoQR);
        params.put("placa_vehiculo", txtPlaca.getText().toString());
        params.put("fecha_desde", FechaDesde);
        params.put("fecha_hasta", FechaHasta);
        params.put("state", "A");
        params.put("frecuencia", "");

        JSONObject parametros = new JSONObject(params);

        String url = "http://52.67.115.36/api/nuevainvitacion/"+idInvitado;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*AlertDialog alertDialog = new
                                AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Excelente");
                        alertDialog.setMessage("Se registró correctamente su invitación");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int
                                            which) {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.show();*/
                        codigoQR fragment = new codigoQR();
                        Bundle bundle = new Bundle();
                        bundle.putString("qr", codigoQR);
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

    public String validarHora(String hora, String inicio_fin){
        String horaFinal=hora;

        if (inicio_fin == "I"){
            if (hora.equals("")){
                horaFinal="00:01";
            }

        }else if (inicio_fin == "F"){
            if (hora.equals("")){
                horaFinal="23:59";
            }
        }
        return horaFinal;
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
