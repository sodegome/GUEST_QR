package com.example.guest_qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class menuInvitacion extends AppCompatActivity {
    private TextView shapeIndividual, shapeGrupal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_invitacion);

        //Referencia a los controles
        shapeIndividual = (TextView) findViewById(R.id.shapeIndividual);
        shapeGrupal = (TextView) findViewById(R.id.shapeGrupal);

        //Llamada a la invitacion individual
        shapeIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), invitacionIndividual.class);
                startActivityForResult(intent, 0);
            }
        });
    }


}
