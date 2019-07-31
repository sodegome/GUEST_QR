package com.example.guest_qr.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private RequestQueue mQueue;
    private String token = "";

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








        return v;
    }

    public void obtenerInvitados(){
        String url = "http://52.67.115.36/api/invitados";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        for(int i=0; i<response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String invitado = jsonObject.getString("name") +" "+ jsonObject.getString("last_name");
                                String celular = "Celular: " + jsonObject.getString("cell");
                                String email = "Email: "+jsonObject.getString("email");

                                lista_invitados.put(invitado,celular+" - "+email);

                                System.out.println("Invitado: "+invitado);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Llena la lista de invitados
                        List<HashMap<String,String>> listInvitados = new ArrayList<>();

                        SimpleAdapter adapter = new SimpleAdapter(getActivity(),listInvitados,R.layout.list_invitados,
                                new String[]{"First line","Second line"},
                                new int[] {R.id.txtItem, R.id.txtSubItem});

                        Iterator it = lista_invitados.entrySet().iterator();

                        while (it.hasNext()){
                            HashMap<String,String> resultMap = new HashMap<>();
                            Map.Entry map = (Map.Entry)it.next();

                            resultMap.put("First line",map.getKey().toString());
                            resultMap.put("Second line",map.getValue().toString());

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
