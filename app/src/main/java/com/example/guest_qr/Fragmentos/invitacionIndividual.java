package com.example.guest_qr.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.guest_qr.GenerarQR;
import com.example.guest_qr.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class invitacionIndividual extends Fragment {

    EditText txtFechaDesde, txtFechaHasta, txtHoraDesde, txtHoraHasta;
    Button btnRegistro;

    //Instancias de calendarios
    Calendar fechaDesde = Calendar.getInstance();
    Calendar fechaHasta = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Variables para obtener la hora y minutos que se permite el ingreso
    final int horaDesde = fechaDesde.get(Calendar.HOUR_OF_DAY);
    final int minutoDesde = fechaDesde.get(Calendar.MINUTE);
    final int horaHasta = fechaDesde.get(Calendar.HOUR_OF_DAY);
    final int minutoHasta = fechaDesde.get(Calendar.MINUTE);

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
        btnRegistro = v.findViewById(R.id.btnRegistro);


        //Llamada a generar QR
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoQR fragment = new codigoQR();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu_izquierdo,fragment)
                        .addToBackStack(null)
                        .commit();
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


    //Obtener hora desde
    private void obtenerHoraDesde(){
        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                txtHoraDesde.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
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
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                txtHoraHasta.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, horaHasta, minutoHasta, false);

        recogerHora.show();
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
