package com.example.guest_qr.Fragmentos;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.guest_qr.R;
import com.example.guest_qr.menuIzquierdo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Invitaciones extends Fragment {

    ListView lstInvitaciones;
    HashMap<String,String> lista_invitaciones;

    String[] strSerial, strEstado;

    private RequestQueue mQueue;
    private String token = "";

    String idInvitado, numeroInvitado, nombreInvitado;
    String serial;

    private OnFragmentInteractionListener mListener;

    public Invitaciones() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invitaciones, container, false);

        mQueue = Volley.newRequestQueue(getActivity());

        //Obtiene el token de la activity
        menuIzquierdo activity = (menuIzquierdo) getActivity();
        token = activity.getToken();

        //Extraer valores de la pantalla anterior
        Bundle bundle = this.getArguments();
        idInvitado = bundle.getString("idInvitado");
        numeroInvitado = bundle.getString("numInvitado");
        nombreInvitado = bundle.getString("nombreInvitado");

        getActivity().setTitle("Invitaciones de "+nombreInvitado);

        lstInvitaciones = v.findViewById(R.id.lstInvitaciones);

        lista_invitaciones = new HashMap<>();
        obtenerInvitaciones(idInvitado);

        lstInvitaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Click en "+i);

                serial = strSerial[i];
                if (strEstado[i].equals("A")) {
                    codigoQR fragment = new codigoQR();
                    Bundle bundle = new Bundle();
                    bundle.putString("qr", serial);
                    bundle.putString("numInvitado", numeroInvitado);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_menu_izquierdo, fragment)
                            .addToBackStack(null)
                            .commit();
                }else{
                    AlertDialog alertDialog = new
                            AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage("Esta invitación se encuentra inactiva.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int
                                        which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });



        return v;
    }

    public void obtenerInvitaciones(String idInvitado){
        String url = "http://52.67.115.36/api/invitaciones/"+idInvitado;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());



                        try {
                            JSONArray dataArray = response.getJSONArray("invitaciones");

                            strSerial = new String[dataArray.length()];
                            strEstado = new String[dataArray.length()];

                            for(int i=0; i<dataArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    System.out.println(jsonObject.toString());

                                    String frecuencia =  jsonObject.getString("frecuencia_id");
                                    String placa = "Placa del vehículo: "+validarNull(( jsonObject.getString("placa_vehiculo")),"-");
                                    String estado = "Estado: " + validarEstado(jsonObject.getString("state"));

                                    String fecha_desde, fecha_hasta;
                                    strSerial[i] = jsonObject.getString("serial");
                                    strEstado[i] = jsonObject.getString("state");

                                    if (frecuencia.equals("null")){
                                        frecuencia = "Invitación esporádica";

                                        fecha_desde = "Fecha desde: " + validarFecha(jsonObject.getString("fecha_desde"),false);
                                        fecha_hasta = "Fecha Hasta: " + validarFecha(jsonObject.getString("fecha_hasta"),false);
                                    }
                                    else{
                                        frecuencia = "Invitación frecuente";

                                        fecha_desde = "Fecha desde: " + validarFecha(jsonObject.getString("fecha_desde"),true);
                                        fecha_hasta = "Fecha Hasta: " + validarFecha(jsonObject.getString("fecha_hasta"),true);
                                    }

                                    lista_invitaciones.put(frecuencia+"/"+i,
                                            placa+"%"+fecha_desde+"%"+fecha_hasta+"%"+estado);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Llena la lista de invitaciones
                            List<HashMap<String,String>> listInvitaciones = new ArrayList<>();

                            SimpleAdapter adapter = new SimpleAdapter(getActivity(),listInvitaciones,R.layout.list_invitaciones,
                                    new String[]{"First line","Second line", "Third line", "Fourth line", "Fifth line"},
                                    new int[] {R.id.txtItem, R.id.txtPlaca, R.id.txtFechaDesde, R.id.txtFechaHasta, R.id.txtEstado});

                            System.out.println(lista_invitaciones.size());
                            Iterator it = lista_invitaciones.entrySet().iterator();

                            while (it.hasNext()){
                                System.out.println("Entra");
                                HashMap<String,String> resultMap = new HashMap<>();
                                Map.Entry map = (Map.Entry)it.next();

                                String[] partes = map.getValue().toString().split("%");

                                resultMap.put("First line",map.getKey().toString().split("/")[0]);
                                resultMap.put("Second line",partes[0]);
                                resultMap.put("Third line",partes[1]);
                                resultMap.put("Fourth line",partes[2]);
                                resultMap.put("Fifth line",partes[3]);

                                listInvitaciones.add(resultMap);
                            }

                            lstInvitaciones.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public String validarNull(String variable, String valor){
        if (variable.equals("null")){
            return valor;
        }
        else{
            return variable;
        }

    }

    public String validarFecha(String fecha,boolean frecuente){
        String[] fechaHora = fecha.split(" ");

        String[] f = fechaHora[0].split("-");
        String h = fechaHora[1];

        String anio = f[0];
        String mes = f[1];
        String dia = f[2];

        String fechaSalida = dia+"/"+mes+"/"+anio;

        if (frecuente == false){
            fechaSalida = fechaSalida + " "+h;
        }
        return fechaSalida;

    }

    public String validarEstado(String estado){
        if (estado.equals("A")){
            estado = "Activo";
        }
        else if(estado.equals("I")){
            estado = "Inactivo";
        }
        return estado;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
