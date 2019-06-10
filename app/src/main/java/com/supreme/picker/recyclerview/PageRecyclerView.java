package com.supreme.picker.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class PageRecyclerView extends RecyclerView {

    public PageRecyclerView(Context context) {
        super(context);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return false;
    }
}
