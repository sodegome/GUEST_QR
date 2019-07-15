package com.example.guest_qr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.widget.ArrayAdapter;

public class listaInvitados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_invitados);
    }

    private List<Invitado> getModel() {
        List<Invitado> list = new ArrayList<Invitado>();
        list.add(get("Invitado 1"));
        list.add(get("Invitado 2"));
        list.add(get("Invitado 3"));
        list.add(get("Invitado 4"));
        list.add(get("Invitado 5"));
        list.add(get("Invitado 6"));
        list.add(get("Invitado 7"));
        list.add(get("Invitado 8"));
        // Initially select one of the items
        list.get(1).setSelected(true);
        return list;
    }

    private Invitado get(String s) {
        return new Invitado(s);
    }

}
