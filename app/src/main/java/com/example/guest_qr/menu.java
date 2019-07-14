package com.example.guest_qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class menu extends AppCompatActivity {
    private TextView shapeRegistrar, shapeInvitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Referencia a los controles
        shapeRegistrar = (TextView) findViewById(R.id.shapeRegistrar);
        shapeInvitar = (TextView) findViewById(R.id.shapeInvitar);

        //Llamada a registrar
        shapeRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Registro.class);
                startActivityForResult(intent, 0);
            }
        });

        //Llamada a invitar
        shapeInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), menuInvitacion.class);
                startActivityForResult(intent, 0);
            }
        });
    }




}
