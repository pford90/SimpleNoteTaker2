package com.peterford.simplenotetaker.listener;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;


public class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Object object);

        void onItemLongClick(View view);
    }

    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    private List<?> mObjects;

    public RecyclerViewClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener, List<? extends Object> objects) {
        mListener = listener;
        mObjects = objects;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if( childView != null && mListener != null & mGestureDetector.onTouchEvent(e)) {
            int position = rv.getChildAdapterPosition(childView);
            mListener.onItemClick(childView, position, mObjects.get(position) );
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
