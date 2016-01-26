package com.rezadiscount.rezadiscount.reza.discount.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sebastienferrand on 1/9/16.
 */
public class BestDealAdapter extends RecyclerView.Adapter<BestDealAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BestDealAdapter(ArrayList<HashMap<String, String>> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BestDealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder((LinearLayout) v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).get(QuickstartPreferences.TAG_ID));
        Log.d("CardView Text", "DataSet " + mDataset.get(position).get(QuickstartPreferences.TAG_ID));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView mTextView;

        public ViewHolder(LinearLayout v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            mTextView = (TextView) v.findViewById(R.id.info_text);


        }


    }
}
