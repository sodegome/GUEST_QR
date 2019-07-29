package com.example.guest_qr.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest_qr.R;


public class home extends Fragment {
    private ImageView imgRegistrar, imgInvitar;

    private OnFragmentInteractionListener mListener;

    public home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Menu principal");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        imgRegistrar = v.findViewById(R.id.imgRegistrar);
        imgInvitar =  v.findViewById(R.id.imgInvitar);

        //Llamada a registrar
        imgRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro fragment = new registro();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu_izquierdo,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Llamada a invitar
        imgInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitacionIndividual fragment = new invitacionIndividual();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_menu_izquierdo,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return v;
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
