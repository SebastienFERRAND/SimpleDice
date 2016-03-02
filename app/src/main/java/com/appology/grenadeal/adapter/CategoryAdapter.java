package com.appology.grenadeal.adapter;

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

import com.appology.grenadeal.Business.Category;
import com.appology.grenadeal.R;
import com.appology.grenadeal.activities.BusinessResults;
import com.appology.grenadeal.utilities.QuickstartPreferences;

import java.util.ArrayList;

/**
 * Adapter of finding a business by category
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> categoryList;
    private Context act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(ArrayList<Category> categoryListP, Context actP) {
        act = actP;
        categoryList = categoryListP;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new CategoryViewHolder((LinearLayout) v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(categoryList.get(position).getName());
        Log.d("CardView Text", "categoryList.get(position).getName() " + categoryList.get(position).getName());
        Log.d("CardView Text", "categoryList.get(position) " + categoryList.get(position));
        Log.d("CardView Text", "position " + position);

        int id = categoryList.get(position).getId();
        holder.setItem(id);
        //holder.currentItem = items.get(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView mTextView;

        public int mId;

        public CategoryViewHolder(LinearLayout v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            mTextView = (TextView) v.findViewById(R.id.info_text);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myIntent = new Intent(act, BusinessResults.class);
                    myIntent.putExtra(QuickstartPreferences.TAG_ID, mId);
                    act.startActivity(myIntent);
                }
            });
        }
        public void setItem(int id) {
            mId = id;
        }
    }
}
