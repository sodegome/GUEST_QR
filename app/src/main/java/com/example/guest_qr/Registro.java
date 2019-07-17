package com.example.guest_qr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import android.view.View;

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

public class Registro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spnTipoId;
    Button btnRegistro;
    EditText txtNombre, txtApellido, txtCelular, txtMail, txtIdentificacion;
    private RequestQueue mQueue;
    private String token = "";

    String[] strTipos;
    List<String> listaTipo;
    ArrayAdapter<String> comboAdapter;
    String tipoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mQueue = Volley.newRequestQueue(this);

        //Obtiene el token de la pantalla anterior
        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");

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


        //Llamada a registrar el invitado
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent (v.getContext(), menu.class);
                startActivityForResult(intent, 0);*/
                posInvitado();
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

    public void posInvitado() {
        //Obtener los valores de los txtBoxs

        Map<String, String> params = new HashMap();

        //Referencia a los controles
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtCelular = (EditText) findViewById(R.id.txtCelular);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtIdentificacion = (EditText) findViewById(R.id.txtIdentificacion);
        params.put("name", txtNombre.getText().toString());
        params.put("last_name", txtApellido.getText().toString());
        params.put("cell", txtCelular.getText().toString());
        params.put("email", txtMail.getText().toString());
        params.put("type_id", tipoId);
        params.put("identification", txtIdentificacion.getText().toString());

        JSONObject parametros = new JSONObject(params);

        String login_url = "http://52.67.115.36/api/nuevoinvitado";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, login_url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog alertDialog = new
                                AlertDialog.Builder(Registro.this).create();
                        alertDialog.setTitle("Excelente");
                        alertDialog.setMessage("Se registró correctamente su invitado");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int
                                            which) {
                                        dialog.dismiss();
                                        Intent menuPrincipal = new
                                                Intent(getBaseContext(), menu.class);
                                        menuPrincipal.putExtra("token", token);
                                        startActivity(menuPrincipal);
                                    }
                                });
                        alertDialog.show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new
                        AlertDialog.Builder(Registro.this).create();
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
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }

    }
