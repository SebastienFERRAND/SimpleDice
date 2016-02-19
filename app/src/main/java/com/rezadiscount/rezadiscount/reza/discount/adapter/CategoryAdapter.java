package com.rezadiscount.rezadiscount.reza.discount.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezadiscount.rezadiscount.R;
import com.rezadiscount.rezadiscount.reza.discount.activities.BusinessResults;
import com.rezadiscount.rezadiscount.reza.discount.utilities.QuickstartPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter of finding a business by category
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> mDataset;
    private Context act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(ArrayList<HashMap<String, String>> myDataset, Context actP) {
        act = actP;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        holder.mTextView.setText(mDataset.get(position).get(QuickstartPreferences.TAG_NAME));
        String id = mDataset.get(position).get(QuickstartPreferences.TAG_ID);
        holder.setItem(id);
        //holder.currentItem = items.get(position);

        Log.d("CardView Text", "DataSet " + mDataset.get(position).get(QuickstartPreferences.TAG_NAME));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView mTextView;

        public String mId;
        public String mName;
        public ClipData.Item currentItem;

        public ViewHolder(LinearLayout v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            mTextView = (TextView) v.findViewById(R.id.info_text);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myIntent = new Intent(act, BusinessResults.class);
                    myIntent.putExtra(QuickstartPreferences.TAG_ID, mId);
                    act.startActivity(myIntent);

                    Log.d("Click", "Id  " + mId);
                }
            });
        }


        public void setItem(String id) {
            mId = id;
        }
    }
}
