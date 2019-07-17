package com.example.guest_qr;

import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private EditText txtUsuario, txtPassword;
    private Button btnLogin;
    String usuario, password;

    //Cola donde se va a guardar los requests a ejecutar
    private RequestQueue mQueue;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);

        //Referencia a los controles
        txtUsuario = (EditText) findViewById(R.id.txtNombre);


        //Obtener valores de los editText
        usuario = txtUsuario.getText().toString();
        password = txtPassword.getText().toString();

        /*
        //Llamada al menu
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), menu.class);
                startActivityForResult(intent, 0);
            }
        });*/
    }

    public void ingresar(View view){
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        Map<String, String> params = new HashMap();
        params.put("username", usuario);
        params.put("password", password);
        JSONObject parametros = new JSONObject(params);
        String login_url = "http://http://52.67.115.36/api/user/login";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, login_url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            token = response.getString("token");
                            Intent menuPrincipal = new
                                    Intent(getBaseContext(), menu.class);
                            menuPrincipal.putExtra("token", token);
                            startActivity(menuPrincipal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new
                        AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Usuario o contraseña incorrectas. Ingrese nuevamente.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int
                                    which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        mQueue.add(request);
    }




}
