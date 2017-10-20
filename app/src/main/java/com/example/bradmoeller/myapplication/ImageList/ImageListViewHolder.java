package com.example.bradmoeller.myapplication.ImageList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by bradmoeller on 9/20/17.
 */

public class ImageListViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public ImageListViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView;
    }
}
