package com.example.guest_qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class menu extends AppCompatActivity {
    private TextView shapeRegistrar, shapeInvitar;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Traer token de la pantalla anterior
        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");


        //Referencia a los controles
        shapeRegistrar = (TextView) findViewById(R.id.shapeRegistrar);
        shapeInvitar = (TextView) findViewById(R.id.shapeInvitar);

        //Llamada a registrar
        shapeRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Registro.class);
                intent.putExtra("token", token);
                startActivityForResult(intent, 0);
            }
        });

        //Llamada a invitar
        shapeInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), invitacionIndividual.class);
                intent.putExtra("token", token);
                startActivityForResult(intent, 0);
            }
        });
    }




}
