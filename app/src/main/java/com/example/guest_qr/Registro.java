package com.example.guest_qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Registro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spnTipoId;
    Button btnRegistro;

    String[] strTipos;
    List<String> listaTipo;
    ArrayAdapter<String> comboAdapter;
    String tipoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Referencia a los controles
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        /*
        Spinner spnTipoId = (Spinner) findViewById(R.id.spnTipoId);
        String[] tipoIdentificacion = {"Cédula","RUC","Pasaporte"};
        spnTipoId.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipoIdentificacion));

*/
        //Llenar spinner del tipo de Identificacion
        spnTipoId = (Spinner) findViewById(R.id.spnTipoId);
        spnTipoId.setOnItemSelectedListener(this);
        listaTipo = new ArrayList<>();
        strTipos = new String[]  {"Cédula","RUC","Pasaporte"};
        Collections.addAll(listaTipo, strTipos);
        comboAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, listaTipo);
        //Cargo el spinner con los datos
        spnTipoId.setAdapter(comboAdapter);


        //Llamada a registrar el vehiculo
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), menu.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnTipoId:
                //Almaceno el nombre del tipo de ID seleccionado
                tipoId = strTipos[position];
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
