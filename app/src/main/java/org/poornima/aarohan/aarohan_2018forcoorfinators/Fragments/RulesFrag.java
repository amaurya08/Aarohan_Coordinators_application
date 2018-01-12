package org.poornima.aarohan.aarohan_2018forcoorfinators.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



;import org.poornima.aarohan.aarohan_2018forcoorfinators.R;

/**
 * Created by ADMIN on 29-Dec-17.
 */

public class RulesFrag extends Fragment
{
    private TextView details;
    public RulesFrag()
    {
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View customview= inflater.inflate(R.layout.frag_rules, container, false);

        fetchDetails();
        return customview;
    }

    private void fetchDetails() {

    }
}
