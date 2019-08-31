package com.example.guest_qr.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.guest_qr.R;
import com.example.guest_qr.menuIzquierdo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Invitados extends Fragment {
    ListView lstInvitados;
    HashMap<String,String> lista_invitados;
    String[] strInvitados;
    String[] strIdInvitados;
    String[] strNumInvitados;

    private RequestQueue mQueue;
    private String token = "";

    String idInvitado, nombreInvitado, numeroInvitado;

    private OnFragmentInteractionListener mListener;

    public Invitados() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Lista de invitados");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invitados, container, false);

        mQueue = Volley.newRequestQueue(getActivity());

        //Obtiene el token de la activity
        menuIzquierdo activity = (menuIzquierdo) getActivity();
        token = activity.getToken();

        lstInvitados = v.findViewById(R.id.lstInvitados);

        lista_invitados = new HashMap<>();
        obtenerInvitados();

        lstInvitados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getId()) {
                    case R.id.lstInvitados:
                        idInvitado = strIdInvitados[i];
                        nombreInvitado = strInvitados[i];
                        numeroInvitado = strNumInvitados[i];

                        Invitaciones fragment = new Invitaciones();
                        Bundle bundle = new Bundle();
                        bundle.putString("idInvitado", idInvitado);
                        bundle.putString("nombreInvitado", nombreInvitado);
                        bundle.putString("numInvitado", numeroInvitado);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_menu_izquierdo,fragment)
                                .addToBackStack(null)
                                .commit();

                        break;

                }
            }
        });

        return v;
    }

    public void obtenerInvitados(){
        String url = "http://52.67.115.36/api/invitados";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        strIdInvitados = new String[response.length()];
                        strInvitados = new String[response.length()];
                        strNumInvitados = new String[response.length()];

                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String invitado = jsonObject.getString("name") +" "+ jsonObject.getString("last_name");
                                String celular = "Celular: " + jsonObject.getString("cell");
                                String email = "Email: "+jsonObject.getString("email");

                                strIdInvitados[i] = jsonObject.getString("id");
                                strInvitados[i] = invitado;
                                strNumInvitados[i] = celular;

                                lista_invitados.put(invitado,celular+"-"+email);

                                System.out.println("Invitado: "+invitado);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Llena la lista de invitados
                        List<HashMap<String,String>> listInvitados = new ArrayList<>();

                        SimpleAdapter adapter = new SimpleAdapter(getActivity(),listInvitados,R.layout.list_invitados,
                                new String[]{"First line","Second line", "Third line"},
                                new int[] {R.id.txtItem, R.id.txtCell, R.id.txtCorreo});

                        Iterator it = lista_invitados.entrySet().iterator();

                        while (it.hasNext()){
                            HashMap<String,String> resultMap = new HashMap<>();
                            Map.Entry map = (Map.Entry)it.next();

                            String[] partes = map.getValue().toString().split("-");

                            resultMap.put("First line",map.getKey().toString());
                            resultMap.put("Second line",partes[0]);
                            resultMap.put("Third line",partes[1]);

                            listInvitados.add(resultMap);
                        }

                        lstInvitados.setAdapter(adapter);



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
