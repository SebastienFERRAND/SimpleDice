package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "label";
    private static final String ARG_PARAM3 = "latitude";
    private static final String ARG_PARAM4 = "longitude";
    private static final String ARG_PARAM5 = "distance";
    private static final String ARG_PARAM6 = "adress";
    private static final String ARG_PARAM7 = "picture";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView idT;
    private TextView labelT;
    private TextView longitudeT;
    private TextView latitudeT;
    private TextView distanceT;
    private TextView pictureT;
    private TextView adressT;


    private static String idS;
    private static String labelS;
    private static Double longitudeS;
    private static Double latitudeS;
    private static String distanceS;
    private static String pictureS;
    private static String adressS;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(String id, String label, double latitude, double longitude,
                                              String distance, String picture, String adress) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, label);
        args.putDouble(ARG_PARAM3, latitude);
        args.putDouble(ARG_PARAM4, longitude);
        args.putString(ARG_PARAM5, distance);
        args.putString(ARG_PARAM6, picture);
        args.putString(ARG_PARAM7, adress);

        idS = id;
        labelS = label;
        latitudeS = latitude;
        longitudeS = longitude;
        distanceS = distance;
        pictureS = picture;
        adressS = adress;

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        idT = (TextView) v.findViewById(R.id.id);
        labelT = (TextView) v.findViewById(R.id.label);
        longitudeT = (TextView) v.findViewById(R.id.longitude);
        latitudeT = (TextView) v.findViewById(R.id.latitude);
        distanceT = (TextView) v.findViewById(R.id.distance);
        pictureT = (TextView) v.findViewById(R.id.picture);
        adressT = (TextView) v.findViewById(R.id.adress);


        idT.setText(idS);
        labelT.setText(labelS);
        latitudeT.setText(latitudeS +"");
        longitudeT.setText(longitudeS +"");
        distanceT.setText(distanceS);
        pictureT.setText(pictureS);
        adressT.setText(adressS);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
