package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;

import org.jetbrains.annotations.NotNull;

import dagger.android.support.AndroidSupportInjection;

public class TopicDetailsFragment extends Fragment {


    // Required empty public constructor
    public TopicDetailsFragment() {}


    @Override
    public void onAttach(@NotNull Context context) {
        // Note: when using Dagger for injecting Fragment objects,
        // inject as early as possible. For this reason, call
        // AndroidInjection.inject() in onAttach(). This also
        // prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic_details, container, false);
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }

}
