package com.example.bradmoeller.myapplication.ImageList;

/**
 * Created by bradmoeller on 10/9/17.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

