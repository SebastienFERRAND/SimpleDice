package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.adapter.BestDealAdapter;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentFavoriteShops extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<HashMap<String, String>> dealList;

    public FragmentFavoriteShops() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        dealList = new ArrayList<>();

        HashMap<String, String> map = new HashMap<>();

        map.put(QuickstartPreferences.TAG_ID, "Test1");

        dealList.add(map);

        HashMap<String, String> map2 = new HashMap<>();

        map2.put(QuickstartPreferences.TAG_ID, "Test2");

        dealList.add(map2);

        HashMap<String, String> map3 = new HashMap<>();

        map3.put(QuickstartPreferences.TAG_ID, "Test3");

        dealList.add(map3);

        //Create the list of Deals
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BestDealAdapter(dealList);
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

}
