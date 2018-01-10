package org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

/**
 * Created by Bhoomika on 10-01-2018.
 */

public class AccoCoordinatorProfileFrag extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View customview= inflater.inflate(R.layout.accocoordinatorprofile, container, false);
        return customview;
    }
}
