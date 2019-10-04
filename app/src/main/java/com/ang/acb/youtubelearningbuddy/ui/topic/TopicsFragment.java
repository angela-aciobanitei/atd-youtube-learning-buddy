package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicsFragment extends Fragment {


    // Required empty public constructor
    public TopicsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

}
