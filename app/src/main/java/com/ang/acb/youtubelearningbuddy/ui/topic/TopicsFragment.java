package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.NavigationController;
import com.ang.acb.youtubelearningbuddy.ui.search.SearchViewModel;
import com.ang.acb.youtubelearningbuddy.ui.search.VideosAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicsFragment extends Fragment {

    private FragmentTopicsBinding binding;
    private TopicsViewModel topicsViewModel;
    private TopicsAdapter topicsAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    // Required empty public constructor
    public TopicsFragment() {}


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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopicsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbarTitle();
        initViewModel();
        initAdapter();
        handleNewTopicCreation();
        populateUi();
    }

    private void setupToolbarTitle() {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar()
                    .setTitle(getString(R.string.action_show_topics));
        }
    }

    private void initViewModel() {
        topicsViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(TopicsViewModel.class);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvTopics.setLayoutManager(layoutManager);
        topicsAdapter = new TopicsAdapter(topicItem ->
                navigationController.navigateToTopicDetails(topicItem.getId()));
        binding.rvTopics.setAdapter(topicsAdapter);
    }

    private void handleNewTopicCreation() {
        binding.newTopicButton.setOnClickListener(view -> createNewTopicDialog());
    }

    private void createNewTopicDialog() {
        // See: https://www.journaldev.com/22153/android-custom-alert-dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View dialogView = getHostActivity().getLayoutInflater()
                .inflate(R.layout.create_new_topic_dialog, null);
        dialogBuilder.setView(dialogView);
        setupDialogButtons(dialogBuilder, dialogView);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        customizeDialogButtons(dialog);
    }

    private void setupDialogButtons(AlertDialog.Builder dialogBuilder, View dialogView){
        // TODO: Positive button is inactive while edit text input is empty.
        final EditText editText = dialogView.findViewById(R.id.dialog_edit_text);
        dialogBuilder.setPositiveButton(R.string.dialog_pos_btn, (dialog, whichButton) -> {
            String input = editText.getText().toString();
            if (input.trim().length() != 0) topicsViewModel.createTopic(input);
            else dialog.dismiss();
        });

        dialogBuilder.setNegativeButton(R.string.dialog_neg_btn, (dialog, whichButton) ->
            dialog.cancel());
    }

    private void customizeDialogButtons(AlertDialog dialog) {
        Button posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posBtn.setBackgroundColor(ContextCompat.getColor(
                getContext(), android.R.color.transparent));
        posBtn.setTextColor(ContextCompat.getColor(
                getContext(),R.color.colorAccent));
        posBtn.setPadding(16, 0, 16, 0);

        Button negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negBtn.setBackgroundColor(ContextCompat.getColor(
                getContext(), android.R.color.transparent));
        negBtn.setTextColor(ContextCompat.getColor(
                getContext(),R.color.colorAccent));
        negBtn.setPadding(16, 0, 16, 0);
    }

    private void populateUi() {
        topicsViewModel.getAllTopics().observe(getViewLifecycleOwner(), result -> {
            binding.setTopicCount((result == null) ? 0 : result.size());
            if (result != null) {
                topicsAdapter.submitList(result);
            }
            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
