package com.example.guest_qr.Fragmentos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;

import java.util.HashMap;
import java.util.Map;

import com.example.guest_qr.Clases.Invitados;
import com.example.guest_qr.R;
import com.example.guest_qr.Registro;
import com.example.guest_qr.*;

import java.util.List;


public class registro extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spnTipoId;
    Button btnRegistro;
    EditText txtNombre, txtApellido, txtCelular, txtMail, txtIdentificacion;
    private RequestQueue mQueue;
    private String token = "";

    String[] strTipos;
    List<String> listaTipo;
    ArrayAdapter<String> comboAdapter;
    String tipoId;


    public registro() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Registro");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registro, container, false);

        mQueue = Volley.newRequestQueue(getActivity());

        //Obtiene el token de la activity
        menuIzquierdo activity = (menuIzquierdo) getActivity();
        token = activity.getToken();

        //Referencia a los controles
        btnRegistro = v.findViewById(R.id.btnRegistro);

        //Referencia a los controles
        txtNombre = v.findViewById(R.id.txtNombre);
        txtApellido = v.findViewById(R.id.txtApellido);
        txtCelular = v.findViewById(R.id.txtCelular);
        txtMail = v.findViewById(R.id.txtMail);
        txtIdentificacion = v.findViewById(R.id.txtIdentificacion);

        //Llenar spinner del tipo de Identificacion
        spnTipoId = v.findViewById(R.id.spnTipoId);
        spnTipoId.setOnItemSelectedListener(this);
        listaTipo = new ArrayList<>();
        strTipos = new String[]  {"Cédula","RUC","Pasaporte"};
        Collections.addAll(listaTipo, strTipos);
        comboAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, listaTipo);
        //Cargo el spinner con los datos
        spnTipoId.setAdapter(comboAdapter);


        //Llamada a registrar el invitado
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent (v.getContext(), menu.class);
                startActivityForResult(intent, 0);*/

                if (validarVacio()){
                    if (validar()){
                        posInvitado();
                    }
                }


            }
        });

        return v;
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


    public void posInvitado() {
        Map<String, String> params = new HashMap();

        params.put("name", txtNombre.getText().toString());
        params.put("last_name", txtApellido.getText().toString());
        params.put("cell", txtCelular.getText().toString());
        params.put("email", txtMail.getText().toString());
        params.put("type_id", getTipoIdentifacion(tipoId));
        params.put("identification", txtIdentificacion.getText().toString());

        JSONObject parametros = new JSONObject(params);

        String url = "http://52.67.115.36/api/nuevoinvitado";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog alertDialog = new
                                AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Excelente");
                        alertDialog.setMessage("Se registró correctamente su invitado");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int
                                            which) {
                                        dialog.dismiss();
                                        home fragment = new home();
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_menu_izquierdo,fragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });
                        alertDialog.show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new
                        AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al registrar el invitado");
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

    public boolean validar(){
        String celular = txtCelular.getText().toString();
        String email = txtMail.getText().toString();
        String identificacion = txtIdentificacion.getText().toString();

        String mensaje = "";

        if (!celular.matches("[0-9]+") || celular.length() != 10) {
            mensaje = mensaje + "* Número de celular inválido. \n";
        }

        if ( !email.contains("@")){
            mensaje = mensaje + "* Email inválido. \n";
        }

        if (!identificacion.matches("[0-9]+")) {
            mensaje = mensaje +"* Número de identificación inválido. \n";
        }

        if(mensaje != ""){
            AlertDialog alertDialog = new
                    AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("¡Error!");
            alertDialog.setMessage("No se puede registrar su invitado debido a: \n" + mensaje);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int
                                which) {
                            dialog.dismiss();
                        }
                    });
            System.out.println(mensaje);
            alertDialog.show();
            return false;
        }
        else{
            System.out.println("True");
            return true;
        }
    }

    public boolean validarVacio(){
        String celular = txtCelular.getText().toString();
        String email = txtMail.getText().toString();
        String identificacion = txtIdentificacion.getText().toString();

        String mensaje = "";

        if (celular.equals("")) {
            mensaje = mensaje + "* Número de celular \n";
        }

        if ( email.equals("")){
            mensaje = mensaje + "* Email  \n";
        }

        if (identificacion.equals("")) {
            mensaje = mensaje +"* Número de identificación \n";
        }

        if(mensaje != ""){
            AlertDialog alertDialog = new
                    AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("¡Error!");
            alertDialog.setMessage("Ingrese \n" + mensaje);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int
                                which) {
                            dialog.dismiss();
                        }
                    });
            System.out.println(mensaje);
            alertDialog.show();
            return false;
        }
        else{
            System.out.println("True");
            return true;
        }
    }

    public String getTipoIdentifacion(String id){
        if(id.equals("Cédula")){
            return "CED";
        }
        else if(id.equals("Pasaporte")){
            return "PAS";
        }
        else{
            return id;
        }

    }

}
