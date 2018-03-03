package com.peterford.simplenotetaker.listener;

import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Peter on 3/3/2018.
 */

public class MainSlidePanelListener implements SlidingUpPanelLayout.PanelSlideListener {

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if(previousState == SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            SlidingUpPanelLayout slideUp = (SlidingUpPanelLayout) panel;
            slideUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }
}
