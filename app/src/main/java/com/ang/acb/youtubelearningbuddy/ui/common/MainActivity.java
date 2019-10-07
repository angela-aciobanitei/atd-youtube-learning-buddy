package com.ang.acb.youtubelearningbuddy.ui.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.databinding.ActivityMainBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


/**
 * An activity that inflates a layout that has a NavHostFragment.
 */
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private ActivityMainBinding binding;

    @Inject
    NavigationController navigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Show video search fragment by default
        if (savedInstanceState == null) {
            navigationController.navigateToSearch();
        }

        // Setup toolbar
        setSupportActionBar(binding.mainToolbar);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search_videos:
                    navigationController.navigateToSearch();
                    return true;

                case R.id.action_show_topics:
                    navigationController.navigateToTopics();
                    return true;

                case R.id.action_show_favorites:
                    // TODO navigationController.navigateToFavorites();
                    return true;
            }

            return false;
        });
    }


}