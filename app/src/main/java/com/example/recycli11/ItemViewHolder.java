package com.example.recycli11;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Ronalyn nanong
 * creates each element in the recycler view
 */
class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView itemName, itemBrandWeight;
    View v;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        itemBrandWeight = itemView.findViewById(R.id.item_brand_weight);

        v = itemView;
    }
}
