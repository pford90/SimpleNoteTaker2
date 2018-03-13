package com.peterford.simplenotetaker.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Peter on 2/20/2018.
 */

public class VerticalSpacingDecoration extends RecyclerView.ItemDecoration{

    private final int verticalSpaceHeight;

    public VerticalSpacingDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
        outRect.top = verticalSpaceHeight;
    }
}
