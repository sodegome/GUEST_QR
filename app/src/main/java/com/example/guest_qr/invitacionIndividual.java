package com.example.guest_qr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import java.util.*;
import android.app.*;
import java.text.*;

public class invitacionIndividual extends AppCompatActivity {
    EditText txtFechaDesde, txtFechaHasta, txtHoraDesde, txtHoraHasta;

    //Instancias de calendarios
    Calendar fechaDesde = Calendar.getInstance();
    Calendar fechaHasta = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora y minutos que se permite el ingreso
    final int horaDesde = fechaDesde.get(Calendar.HOUR_OF_DAY);
    final int minutoDesde = fechaDesde.get(Calendar.MINUTE);
    final int horaHasta = fechaDesde.get(Calendar.HOUR_OF_DAY);
    final int minutoHasta = fechaDesde.get(Calendar.MINUTE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitacion_individual);

        //Referencia de los controles
        txtFechaDesde = findViewById(R.id.txtFechaDesde);
        txtFechaHasta = findViewById(R.id.txtFechaHasta);
        txtHoraDesde = findViewById(R.id.txtHoraDesde);
        txtHoraHasta = findViewById(R.id.txtHoraHasta);

        //Fecha Desde
        txtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(invitacionIndividual.this, fDesde, fechaDesde
                        .get(Calendar.YEAR), fechaDesde.get(Calendar.MONTH),
                        fechaDesde.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Fecha Hasta
        txtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(invitacionIndividual.this, fHasta, fechaHasta
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
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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

}
