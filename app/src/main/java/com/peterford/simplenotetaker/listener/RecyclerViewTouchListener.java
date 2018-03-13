package com.peterford.simplenotetaker.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;


public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private final String TAG = RecyclerViewTouchListener.class.getSimpleName();

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Object object);

        void onItemLongClick(View view, int position, Object object);
    }

    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private List<?> mObjects;

    public RecyclerViewTouchListener(Context context, RecyclerView recyclerView, SlidingUpPanelLayout slidingUpPanelLayout, OnItemClickListener listener, List<? extends Object> objects) {
        mListener = listener;
        mRecyclerView = recyclerView;
        mObjects = objects;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.v(TAG, "INSIDE LONG PRESS");
                View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if( childView != null && mListener != null) {
                    Log.v(TAG, "CHILD VIEW IS NOT NULL");
                    int position = recyclerView.getChildAdapterPosition(childView);
                    Log.v(TAG, "CHILD VIEW POSITION : " + position);
                    mListener.onItemLongClick(childView, position, mObjects.get(position) );
                }

            }
        });
        mSlidingUpPanelLayout = slidingUpPanelLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.v(TAG, "INSIDE INTERCEPT TOUCH EVENT");
        if( mSlidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN ) {
            Log.v(TAG, "HIDING PANEL STATE");
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            return true;
        } else {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null & mGestureDetector.onTouchEvent(e)) {
                int position = rv.getChildAdapterPosition(childView);
                mListener.onItemClick(childView, position, mObjects.get(position));
                return true;
            }
            return false;
        }
    }



    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
