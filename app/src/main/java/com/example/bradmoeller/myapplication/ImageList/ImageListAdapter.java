package com.example.bradmoeller.myapplication.ImageList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.example.bradmoeller.myapplication.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by bradmoeller on 9/20/17.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListViewHolder> implements ItemTouchHelperAdapter {

    private RequestManager glide;
    private List<String> data;


    public ImageListAdapter(RequestManager glide, List<String> urls) {
        this.glide = glide;
        data = urls;
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageListViewHolder holder, int position) {
        glide.load(data.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
}
