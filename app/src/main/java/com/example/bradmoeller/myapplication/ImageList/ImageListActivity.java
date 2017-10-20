package com.example.bradmoeller.myapplication.ImageList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.bumptech.glide.Glide;
import com.example.bradmoeller.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradmoeller on 9/20/17.
 */

public class ImageListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ImageListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_list);
        setTitle("Image List Activity");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ImageListAdapter(Glide.with(this), createImageArray());
        recyclerView.setAdapter(adapter);

        MyItemTouchCallback touchCallback = new MyItemTouchCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private List<String> createImageArray() {
        List<String> result = new ArrayList<>();
        result.add("http://placehold.it/640x480&text=image1");
        result.add("http://placehold.it/640x480&text=image2");
        result.add("http://placehold.it/640x480&text=image3");
        result.add("http://placehold.it/640x480&text=image4");
        result.add("http://placehold.it/640x480&text=image5");
        result.add("http://placehold.it/640x480&text=image6");
        result.add("http://placehold.it/640x480&text=image7");
        result.add("http://placehold.it/640x480&text=image8");
        result.add("http://placehold.it/640x480&text=image9");
        result.add("http://placehold.it/640x480&text=image0");
        result.add("http://placehold.it/640x480&text=image10");
        result.add("http://placehold.it/640x480&text=image12");
        result.add("http://placehold.it/640x480&text=image13");
        result.add("http://placehold.it/640x480&text=image14");
        result.add("http://placehold.it/640x480&text=image15");
        result.add("http://placehold.it/640x480&text=image16");
        result.add("http://placehold.it/640x480&text=image17");
        result.add("http://placehold.it/640x480&text=image18");
        result.add("http://placehold.it/640x480&text=image19");
        result.add("http://placehold.it/640x480&text=image20");
        return result;
    }

    class MyItemTouchCallback extends ItemTouchHelper.Callback {

        private ItemTouchHelperAdapter mAdapter;

        public MyItemTouchCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }



        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }
}
