package com.example.guest_qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class invitacionGrupal extends AppCompatActivity {
    Button btnInvitados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitacion_grupal);

        //Referencia de los controles
        btnInvitados = (Button) findViewById(R.id.btnInvitados);

        //Llamada al menu
        btnInvitados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), listaInvitados.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
